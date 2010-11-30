/**
 * 
 */

package org.jhove2.config.spring;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.format.riff.Chunk;
import org.jhove2.module.format.riff.ChunkFactory;

/** Spring factory for RIFF chunks.
 * 
 * @author slabrams
 */
public class SpringChunkFactory
        implements ChunkFactory
{
    /** Map from chunk identifiers to Spring bean names. */
    protected static ConcurrentMap<String, String> chunkMap;

    /** Get a RIFF chunk based on its identifier.
     * @param identifier Chunk identifier
     * @return Chunk
     * @see org.jhove2.module.format.riff.ChunkFactory#getChunk(java.lang.String)
     */
    @Override
    public Chunk getChunk(String identifier)
        throws JHOVE2Exception
    {
        Chunk chunk = null;
        String bean = SpringChunkFactory.getChunkMap().get(identifier);
        if (bean != null) {
            chunk = SpringConfigInfo.getReportable(Chunk.class, bean);
        }
        else {
            /* Instantiate the generic chunk to handle chunks without
             * specific classes.
             */
            bean = SpringChunkFactory.getChunkMap().get("_J2_");
            if (bean != null) {
                chunk = SpringConfigInfo.getReportable(Chunk.class, bean);
                if (chunk != null) {
                    chunk.setIdentifier(identifier);
                }
            }
        }
        return chunk;
    }  

    /**
     * Gets the mapping from chunk identifiers to Spring beans. Initializes the
     * static map on first invocation.
     * @return Map from chunk identifiers to Spring bean name
     * @throws JHOVE2Exception
     */
    public static ConcurrentMap<String, String> getChunkMap()
        throws JHOVE2Exception
    {
        if (chunkMap == null) {
            chunkMap = new ConcurrentHashMap<String, String>();
            
            /* Use Spring to get instances of all objects inheriting from
             * GenericChunk.
             */
            Map<String, Object> map =
                SpringConfigInfo.getObjectsForType(Chunk.class);
            /* For each of the chunk... */
            for (Entry<String, Object> entry : map.entrySet()) {
                /* Get the Spring bean name for the chunk. */
                String bean = entry.getKey();
                
                /* Get the chunk identifier. */
                Chunk chunk = (Chunk) entry.getValue();
                String identifier = chunk.getIdentifier();
                
                /* Add an entry into the identifier-to-bean map */
                chunkMap.put(identifier, bean);
            }
        }
        return chunkMap;
    }
}
