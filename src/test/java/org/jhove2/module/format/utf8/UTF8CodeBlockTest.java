/**
 * 
 */
package org.jhove2.module.format.utf8;


import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.annotation.Resource;

import org.jhove2.module.format.utf8.unicode.CodeBlock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mstrong
 *
 */
public class UTF8CodeBlockTest extends UTF8ModuleTestBase {

    private String codeBlockTestFile;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        parse(codeBlockTestFile);
    }

    /**
     * Test method for UTF8 Parser 
     */
    @Test
    public void testCodeBlock() {
        int st = 0xFE70;
        int en = 0xFEFF;
        String name = "Arabic Presentation Forms-B";
        CodeBlock block = new CodeBlock(st, en, name);

        Set<CodeBlock> codeBlocks = testUtf8Module.getCodeBlocks();
        
        assertTrue("CodeBlock does not contain " + block.toString(), codeBlocks.contains(block));
    } 

    @After
    public void tearDown() {
        display();
    }

    public String getCodeBlockTestFile() {
        return codeBlockTestFile;
    }

    @Resource
    public void setCodeBlockTestFile(String codeBlockTestFile) {
        this.codeBlockTestFile = codeBlockTestFile;
    }


}
