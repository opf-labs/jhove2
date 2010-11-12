<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs xd"
    xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
    xmlns:j2="http://jhove2.org/xsd/1.0.0" 
    xmlns:mets="http://www.loc.gov/METS/"
    version="2.0">
    <xd:doc scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Aug 10, 2010</xd:p>
            <xd:p><xd:b>Author:</xd:b> Matt Stoeffler</xd:p>
            <xd:p>This stylesheet is designed to convert jhove2 output xml to mets.</xd:p>
        </xd:desc>
    </xd:doc>
    
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
    
    <!-- included modules -->
    <xsl:include href="jhove2_to_mets-amdSec-v1.xsl"/>
    
    <!-- keys -->
    <xsl:key 
        name="GetModbyRName" 
        match="j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/Modules/Module']" 
        use="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value"/>
    <xsl:key 
        name="GetModbyScope" 
        match="j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/Modules/Module']" 
        use="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/module/Module/Scope']/j2:value"/>
    
    
    
    <!-- params & variables -->
    
    <xsl:variable name="GenAmdID" select="concat('AmdSec_Generic_',generate-id(/j2:jhove2/j2:feature))"/>
    
    
    <!-- docID populates /mets/metsHdr/metsDocumentID, with an auto-generated default -->
    <!-- /descendant::j2:feature[@ftid='http://jhove2.org/terms/reportable/org/jhove2/app/JHOVE2CommandLine'] -->
    <xsl:param name="docID" select="generate-id(//j2:feature[@ftid='http://jhove2.org/terms/reportable/org/jhove2/app/JHOVE2CommandLine'])"/>
    
    <xsl:variable name="jhoveDateTime">
        <xsl:choose>
            <xsl:when test="//j2:feature[@ftid='http://jhove2.org/terms/reportable/org/jhove2/app/JHOVE2CommandLine']/j2:features/j2:feature[@name='DateTime']">
                <xsl:value-of select="//j2:feature[@ftid='http://jhove2.org/terms/reportable/org/jhove2/app/JHOVE2CommandLine']/j2:features/j2:feature[@name='DateTime']/j2:value"/>
            </xsl:when>
            <xsl:otherwise><xsl:value-of select="current-dateTime()"/></xsl:otherwise>
        </xsl:choose>
        
    </xsl:variable>
    
    
    
    
    <xsl:template name="addDebugComment">
     <!--   <xsl:param name="nodeType" select="((replace(@ftid,'.+/([A-Za-z]+$)','$1')),@name)[1]"/>-->
        <xsl:param name="nodeType" select="if (@ftid) then replace(@ftid,'.+/([A-Za-z]+$)','$1') else @name"/>
        <xsl:comment xml:space="preserve"> <xsl:value-of select="$nodeType"/> </xsl:comment>
    </xsl:template>
    
    
    
    <xsl:template match="/j2:jhove2">
        <mets:mets xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="xsi:schemaLocation">
                <xsl:text xml:space="preserve">http://www.loc.gov/METS/ http://www.loc.gov/standards/mets/mets.xsd</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="OBJID"><xsl:value-of select="generate-id()"/></xsl:attribute>
            <xsl:attribute name="TYPE">jhove2_output</xsl:attribute>
            
            <!-- metsHdr -->
            
            <mets:metsHdr xmlns:mets="http://www.loc.gov/METS/">
                <xsl:attribute name="CREATEDATE" select="current-dateTime()"/>
                <!--<xsl:attribute name="RECORDSTATUS">[?NEED VALUE HERE?]</xsl:attribute>-->
                <mets:agent xmlns:mets="http://www.loc.gov/METS/" ROLE="CREATOR">
                    <mets:name xmlns:mets="http://www.loc.gov/METS/">jhove2_to_mets-v1.xsl</mets:name>
                </mets:agent>
                <mets:metsDocumentID xmlns:mets="http://www.loc.gov/METS/"><xsl:value-of select="$docID"/></mets:metsDocumentID>
            </mets:metsHdr>
            
            
            <!-- first j2:feature is the root of our tree -->
            <!-- root Sources -->
            <!--           
                Draft CV for mets TYPE:
                FileSource
                DirectorySource
                ClumpSource
                FileSetSource
                URLSource
            -->
          
              
        
                <!-- amdSec -->
                <xsl:call-template name="make_amdSec"/>
                
                <!-- fileSec -->
                <xsl:call-template name="make_fileSec"/>
                
                <!-- structMap -->
                <xsl:call-template name="make_structMap"/>
            
        </mets:mets>
    </xsl:template>
    
    
   
    <!-- ===================================== -->
    <!--  Make fileSec main template           -->
    <!-- ===================================== -->
    
    <xsl:template name="make_fileSec">
      <!-- our current context is /j2:jhove2/j2:feature  : Create a default fileGrp -->
        <mets:fileSec xmlns:mets="http://www.loc.gov/METS/">
            <mets:fileGrp xmlns:mets="http://www.loc.gov/METS/">
                <xsl:attribute name="ID">FileGrp_root</xsl:attribute>
                <!-- if we have childSources, process. If not, what?  a File? -->
              <!-- <xsl:choose>
                   <xsl:when test="j2:features/j2:feature[@name='ChildSources']">
                    <xsl:apply-templates
                        select="j2:features/j2:feature[@name='ChildSources']/j2:features/j2:feature[@name='ChildSource']" 
                        mode="file_sec"/>
                   </xsl:when>
                   <xsl:otherwise>?</xsl:otherwise>
                   </xsl:choose>-->
                <xsl:apply-templates mode="file_sec"/>
            </mets:fileGrp>
        </mets:fileSec>
    </xsl:template>


    <!-- ===================================== -->
    <!--  Make structMap main template         -->
    <!-- ===================================== -->

    <xsl:template name="make_structMap">
        <!-- our current context is /j2:jhove2  -->
        <mets:structMap xmlns:mets="http://www.loc.gov/METS/">
                <xsl:apply-templates mode="struct_map"/>
        </mets:structMap>
    </xsl:template>
    
    
    <!-- ===================================== -->
    <!--  fileSec nodes                        -->
    <!-- ===================================== -->


    
    <!-- FileSource in fileSec; 
        That is, any ChildSource or root Source that is of type FileSource 
    -->
    
    <!-- If it has descendant ZipFileSource or ZipDirectorySource, it is a zip container -->
    
    <!-- If it has j2:features/j2:feature[@name='ChildSources']/j2:features/j2:feature[@name='ChildSource']
        [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ByteStreamSource'], then [it likely is a tiff and]
        if needs it's bytestream processed.
    -->
    <xsl:template 
        match="j2:feature[@name='ChildSource']
        [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/FileSource']
        |
        j2:jhove2/j2:feature[j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value[.='FileSource']]" 
        mode="file_sec">
 
            <xsl:choose>
                <!-- do I have descendant ZipFileSource or ZipDirectorySource features; then I'm a zip Container -->
                <xsl:when 
                    test="descendant::j2:feature[@name='ChildSource']
                    [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ZipFileSource'] or 
                    descendant::j2:feature[@name='ChildSource']
                    [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ZipDirectorySource']">
                    <xsl:call-template name="addDebugComment">
                        <xsl:with-param name="nodeType">Zip Container</xsl:with-param>
                    </xsl:call-template>
                </xsl:when>
                <!-- do I have direct descendant ByteStreamSource features; then I'm a [tiff] file with embedded streams -->
                <xsl:when test="j2:features/j2:feature[@name='ChildSources']/j2:features/j2:feature[@name='ChildSource']
                    [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ByteStreamSource']">
                    <xsl:call-template name="addDebugComment">
                        <xsl:with-param name="nodeType">FileSource with embedded ByteStream[s]</xsl:with-param>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="addDebugComment"/>
                </xsl:otherwise>
            </xsl:choose>
        
        <mets:file xmlns:mets="http://www.loc.gov/METS/">
            <!-- ID -->
            <xsl:attribute name="ID" select="concat('fileSec_',generate-id())"/>
        <!-- CHECKSUM/CHECKSUMTYPE: 
            checksum, in this priority order: 
            CRC32, Adler-32, MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512 -->
        <xsl:if 
            test="j2:features/j2:feature[@name='Modules']/j2:features/j2:feature[@name='Module']/j2:features/j2:feature[@name='Digests']">
            <xsl:for-each 
                select="j2:features/j2:feature[@name='Modules']/j2:features/j2:feature[@name='Module']/j2:features/j2:feature[@name='Digests']">
                <xsl:for-each select="(descendant::j2:feature[@name='Digest'][matches(j2:value,'\[CRC32\]')],
                descendant::j2:feature[@name='Digest'][matches(j2:value,'\[Adler-32\]')],
                descendant::j2:feature[@name='Digest'][matches(j2:value,'\[MD2\]')],
                descendant::j2:feature[@name='Digest'][matches(j2:value,'\[MD5\]')],
                descendant::j2:feature[@name='Digest'][matches(j2:value,'\[SHA-1\]')],
                descendant::j2:feature[@name='Digest'][matches(j2:value,'\[SHA-256\]')],
                descendant::j2:feature[@name='Digest'][matches(j2:value,'\[SHA-384\]')],
                descendant::j2:feature[@name='Digest'][matches(j2:value,'\[SHA-512\]')])[1]">
                <xsl:attribute name="CHECKSUM">
                    <xsl:value-of select="substring-after(j2:value,' ')"/>
                </xsl:attribute>
                <xsl:attribute name="CHECKSUMTYPE">
                    <xsl:value-of select="substring-before(substring-after(j2:value,'['),']')"/>
                </xsl:attribute>
                </xsl:for-each>
            </xsl:for-each>
        </xsl:if>
        
        <!-- SIZE -->
        <xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/FileSource/Size']">
            <xsl:attribute name="SIZE">
                <xsl:value-of 
                    select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/FileSource/Size']/j2:value"/>
            </xsl:attribute>
        </xsl:if>
            
            <!-- FLocat -->
            <xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/FileSource/Path']">
                <mets:FLocat xmlns:mets="http://www.loc.gov/METS/" xmlns:ns2="http://www.w3.org/1999/xlink">
                    <xsl:attribute name="LOCTYPE">OTHER</xsl:attribute>
                    <xsl:attribute name="OTHERLOCTYPE">FILESYSTEM</xsl:attribute>
                    
                    <xsl:attribute name="ns2:href">
                        <xsl:value-of 
                            select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/FileSource/Path']/j2:value"/>
                    </xsl:attribute>
                </mets:FLocat>
            </xsl:if>
            
           
                <!-- do I have descendant ZipFileSource or ZipDirectorySource features; then add a transformFile node -->
                <xsl:if 
                    test="descendant::j2:feature[@name='ChildSource']
                    [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ZipFileSource'] or 
                    descendant::j2:feature[@name='ChildSource']
                    [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ZipDirectorySource']">
                    <mets:transformFile xmlns:mets="http://www.loc.gov/METS/" 
                        TRANSFORMTYPE="decompression" 
                        TRANSFORMALGORITHM="UNZIP" 
                        TRANSFORMORDER="1"/>
                </xsl:if>
              
            <!-- process the ChildSource nodes, if any -->
            <xsl:apply-templates
                select="j2:features/j2:feature[@name='ChildSources']/j2:features/j2:feature[@name='ChildSource']" 
                mode="file_sec"/>
        </mets:file>
    </xsl:template>
    
    <!-- FileSource in fileSec is ByteStreamSource; suppress for now. -->
    <xsl:template match="j2:feature[@name='ChildSource']
        [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ByteStreamSource']" mode="file_sec"/>
    
    
    
    <!-- DirectorySource in fileSec -->
    <xsl:template 
        match="j2:feature[@name='ChildSource'][@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/DirectorySource']
        | 
        j2:jhove2/j2:feature[j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value[.='DirectorySource']]" 
        mode="file_sec">
        
        <xsl:call-template name="addDebugComment"/>
        
        <mets:fileGrp xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="ID" select="concat('fileSec_',generate-id())"/>
            
            <!-- We don't process any ClumpSource children within the DirectorySource fileGrp; just the non-clump ChildSource nodes -->
            <xsl:apply-templates 
                select="j2:features/j2:feature[@name='ChildSources']/j2:features/j2:feature[@name='ChildSource']
                [not(@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ClumpSource')]" mode="file_sec"/>
        </mets:fileGrp>
        
        <!-- We process any ClumpSource children outside the DirectorySource fileGrp -->
        <xsl:apply-templates 
            select="j2:features/j2:feature[@name='ChildSources']/j2:features/j2:feature[@name='ChildSource']
            [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ClumpSource']" mode="file_sec"/>
       </xsl:template>

    <!-- ClumpSource in fileSec -->
    <xsl:template 
        match="j2:feature[@name='ChildSource'][@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ClumpSource']
        |
        j2:jhove2/j2:feature[j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value[.='ClumpSource']]" 
        mode="file_sec">
        
        <xsl:call-template name="addDebugComment"/>
        
        
        <mets:fileGrp xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="ID" select="concat('fileSec_',generate-id())"/>
            
            <!-- process the ChildSource nodes -->
            <xsl:apply-templates select="j2:features/j2:feature[@name='ChildSources']" mode="file_sec"/>
        </mets:fileGrp>
    </xsl:template>
    
    <!-- ChildSource ZipFileSource in fileSec -->
    <xsl:template 
        match="j2:feature[@name='ChildSource'][@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ZipFileSource']" 
        mode="file_sec">
       
        <xsl:call-template name="addDebugComment"/>
        
        <mets:file xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="ID" select="concat('fileSec_',generate-id())"/>
            <xsl:attribute name="OWNERID" select="j2:features/j2:feature[@name='Path']/j2:value"></xsl:attribute>
        </mets:file>
    </xsl:template>
    
    <!-- ChildSource ZipDirectorySource in fileSec -->
    <xsl:template 
        match="j2:feature[@name='ChildSource'][@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ZipDirectorySource']" 
        mode="file_sec">
       <!-- <xsl:text>&#x0a;        </xsl:text>
        <xsl:comment>Embedded zip stream: directory</xsl:comment>
        <xsl:text>&#x0a;        </xsl:text>-->
        
        <!-- The zip directory contents are processed to a flat file stucture -->
        
       <!-- <mets:file xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="ID" select="concat('fileSec_',generate-id())"/>
            <xsl:attribute name="OWNERID" select="j2:features/j2:feature[@name='Path']/j2:value"></xsl:attribute>
        </mets:file>-->
        
        <!-- process the ChildSource nodes -->
        <xsl:apply-templates select="j2:features/j2:feature[@name='ChildSources']" mode="file_sec"/>
    </xsl:template>

    <!-- FileSetSource in fileSec -->
    <xsl:template 
        match="j2:jhove2/j2:feature[j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value[.='FileSetSource']]" 
        mode="file_sec">
        
       
        <xsl:call-template name="addDebugComment"/>
        
        <mets:fileGrp xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="ID" select="concat('fileSec_',generate-id())"/>
            
            <!-- we have to wrap each 'child' of the FileSet in a fileGrp to keep things valid in METS -->
            <xsl:for-each 
                select="j2:features/j2:feature[@name='ChildSources']/j2:features/j2:feature[@name='ChildSource']">
                <mets:fileGrp xmlns:mets="http://www.loc.gov/METS/">
                    <xsl:attribute name="ID" select="concat('fileSec_FileSet_',generate-id())"/>
                    <xsl:apply-templates select="." mode="file_sec"/>
                </mets:fileGrp>
            </xsl:for-each>
        </mets:fileGrp>
    </xsl:template>
    
    <!-- URLSource in fileSec -->
    <xsl:template 
        match="j2:feature[@name='ChildSource'][@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/URLSource']
        |
        j2:jhove2/j2:feature[j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value[.='URLSource']]" 
        mode="file_sec">
       
        <xsl:call-template name="addDebugComment"/>
        
        
        <mets:file xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="ID" select="concat('fileSec_',generate-id())"/>
            <xsl:attribute name="OWNERID" select="j2:features/j2:feature[@name='SourceName']/j2:value"></xsl:attribute>
            <mets:FLocat xmlns:mets="http://www.loc.gov/METS/" xmlns:ns2="http://www.w3.org/1999/xlink">
                <xsl:attribute name="LOCTYPE">URL</xsl:attribute>
                <xsl:attribute name="ns2:href">
                    <xsl:value-of 
                        select="j2:features/j2:feature[@name='SourceName']/j2:value"/>
                </xsl:attribute>
            </mets:FLocat>
        </mets:file>
    </xsl:template>
    
    

    <xsl:template 
        match="j2:feature[@name='ChildSources']" mode="file_sec">
        <xsl:apply-templates select="j2:features/j2:feature[@name='ChildSource']" mode="file_sec"/>
    </xsl:template>
    
    
    
    <!-- ===================================== -->
    <!--  structMap nodes                      -->
    <!-- ===================================== -->
    
    <!-- fileSource in structMap  -->
    <xsl:template 
        match="j2:feature[@name='ChildSource']
        [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/FileSource']
        | 
        j2:jhove2/j2:feature[j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value[.='FileSource']]" 
        mode="struct_map">
        
        <xsl:variable name="myMods" select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/Modules']"/>
    
        <mets:div xmlns:mets="http://www.loc.gov/METS/" TYPE="FileSource">
            <!-- ID -->
            <xsl:attribute name="ID"><xsl:value-of select="concat('structMap_',generate-id())"/></xsl:attribute>
            
            <!-- TYPE -->
            <xsl:attribute name="TYPE">
                <xsl:choose>
                    <!-- We must infer if we are a zip file -->
                    <xsl:when test="descendant::j2:feature[@name='ChildSource']
                        [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ZipFileSource'] or 
                        descendant::j2:feature[@name='ChildSource']
                        [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ZipDirectorySource']">ZipFile</xsl:when>
                    <xsl:otherwise>
                        <!-- Non-zips can use reportablename -->
                        <xsl:value-of 
                            select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            
            <!-- LABEL -->
            <xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/NamedSource/SourceName']">
                    <xsl:attribute name="LABEL">
                        <xsl:value-of 
                            select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/NamedSource/SourceName']/j2:value"/>
                    </xsl:attribute>   
            </xsl:if>
            <xsl:attribute name="ADMID"><xsl:value-of separator=" " select="concat('amdSec_',generate-id()),$GenAmdID"/></xsl:attribute>
            
            <!-- fptr -->
            
          
            <!--<xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/FileSource/Path']">-->
            <xsl:choose>
                <!-- if there are assocaited bytestreams, we create multiple fptr nodes for each bytestream -->
                <xsl:when test="j2:features/j2:feature[@name='ChildSources']/j2:features/j2:feature[@name='ChildSource']
                    [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ByteStreamSource']">
                    <xsl:variable name="myFileRef"><xsl:value-of select="concat('fileSec_',generate-id())"/></xsl:variable>
                    <xsl:variable name="myADMID"><xsl:value-of select="concat('amdSec_',generate-id())"/></xsl:variable>
                    <xsl:for-each select="j2:features/j2:feature[@name='ChildSources']/j2:features/j2:feature[@name='ChildSource']
                        [@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ByteStreamSource']">
                        <mets:fptr xmlns:mets="http://www.loc.gov/METS/">
                            <xsl:attribute name="FILEID">
                                <xsl:value-of select="$myFileRef"/>
                            </xsl:attribute> 
                                <mets:area>
                                    <xsl:attribute name="ID">
                                        <xsl:value-of select="concat('structMap_',generate-id())"/>
                                    </xsl:attribute>
                                    <xsl:attribute name="FILEID">
                                        <xsl:value-of select="$myFileRef"/>
                                    </xsl:attribute> 
                                    <xsl:attribute name="ADMID">
                                        <xsl:value-of separator=" " select="concat('amdSec_',generate-id()),$GenAmdID"/>
                                    </xsl:attribute>
                                    
                                    <xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/StartingOffset']">
                                        <xsl:attribute name="BEGIN">
                                            <xsl:value-of 
                                                select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/StartingOffset']/j2:value"></xsl:value-of>
                                        </xsl:attribute>
                                        <xsl:attribute name="BETYPE">BYTE</xsl:attribute>
                                    </xsl:if>
                                    
                                 <!-- is there and end offset? -->
                                    <xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/ByteStreamSource/EndingOffset']">
                                        <xsl:attribute name="END">
                                            <xsl:value-of 
                                                select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/ByteStreamSource/EndingOffset']/j2:value"></xsl:value-of>
                                        </xsl:attribute>
                                    </xsl:if>
                                    
                                </mets:area>
                            
                        </mets:fptr>
                    </xsl:for-each>
                </xsl:when>
                <!-- if we are a regular fileSource for file or zip file, just a single fptr will do -->
                <xsl:otherwise>
                    <mets:fptr xmlns:mets="http://www.loc.gov/METS/">
                        <xsl:attribute name="FILEID">
                            <xsl:value-of select="concat('fileSec_',generate-id())"/>
                        </xsl:attribute> 
                    </mets:fptr>
                </xsl:otherwise>
            </xsl:choose>
           <!-- </xsl:if>-->
            
            <!-- process the ChildSource nodes that are not ByteStreams-->

            <xsl:apply-templates
                select="j2:features/j2:feature[@name='ChildSources']/j2:features/j2:feature[@name='ChildSource']
                [not(@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ByteStreamSource')]" 
                mode="struct_map"/>  
            
        </mets:div>
    </xsl:template>
    
    <!-- DirectorySource in structMap -->
    <xsl:template 
        match="j2:feature[@name='ChildSource'][@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/DirectorySource']
        | 
        j2:jhove2/j2:feature[j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value[.='DirectorySource']]" 
        mode="struct_map">
        
        
        <mets:div xmlns:mets="http://www.loc.gov/METS/"  TYPE="DirectorySource">
           <xsl:attribute name="ID"><xsl:value-of select="concat('structMap_',generate-id())"/></xsl:attribute>
            <xsl:attribute name="ADMID"><xsl:value-of separator=" " select="concat('amdSec_',generate-id()),$GenAmdID"/></xsl:attribute>
            <!-- process the ChildSource nodes -->
            <xsl:apply-templates select="j2:features/j2:feature[@name='ChildSources']" mode="struct_map"/>
        </mets:div>
    </xsl:template>
    
    
    
    <!-- ChildSource ZipFileSource in structMap -->
    <xsl:template 
        match="j2:feature[@name='ChildSource'][@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ZipFileSource']" 
        mode="struct_map">        
        
        <mets:div xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="ID"><xsl:value-of select="concat('structMap_',generate-id())"/></xsl:attribute>  
            <xsl:attribute name="TYPE">EmbeddedFile</xsl:attribute>
            <xsl:attribute name="LABEL" select="j2:features/j2:feature[@name='Path']/j2:value"></xsl:attribute>
            <xsl:attribute name="ADMID"><xsl:value-of separator=" " select="concat('amdSec_',generate-id()),$GenAmdID"/></xsl:attribute>
        </mets:div>
    </xsl:template>
    
    <!-- ChildSource ZipDirectorySource in structMap -->
    <xsl:template 
        match="j2:feature[@name='ChildSource'][@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ZipDirectorySource']" 
        mode="struct_map">
       
       
        <mets:div xmlns:mets="http://www.loc.gov/METS/">
            <!-- ID -->
            <xsl:attribute name="ID"><xsl:value-of select="concat('structMap_',generate-id())"/></xsl:attribute>
            
            <xsl:attribute name="TYPE">ZipDirectory</xsl:attribute>
            <xsl:attribute name="LABEL" select="j2:features/j2:feature[@name='Path']/j2:value"></xsl:attribute>
            <xsl:attribute name="ADMID"><xsl:value-of separator=" " select="concat('amdSec_',generate-id()),$GenAmdID"/></xsl:attribute>
            <xsl:apply-templates select="j2:features/j2:feature[@name='ChildSources']" mode="struct_map"/>
        </mets:div>
    </xsl:template>
    
    
    
    
    
    <!-- ClumpSource in structMap -->
    <xsl:template 
        match="j2:feature[@name='ChildSource'][@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/ClumpSource']
        | 
        j2:jhove2/j2:feature[j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value[.='ClumpSource']]" 
        mode="struct_map">
        
        <mets:div TYPE="ClumpSource" xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="ID">
              <xsl:value-of select="concat('structMap_',generate-id())"/>  
            </xsl:attribute>
            <xsl:attribute name="ADMID"><xsl:value-of separator=" " select="concat('amdSec_',generate-id()),$GenAmdID"/></xsl:attribute>
            
            <!-- process the ChildSource nodes -->
            <xsl:apply-templates select="j2:features/j2:feature[@name='ChildSources']" mode="struct_map"/>
        </mets:div>
    </xsl:template>
    
    
    <!-- URLSource in structMap -->
    <xsl:template 
        match="j2:feature[@name='ChildSource'][@ftid='http://jhove2.org/terms/reportable/org/jhove2/core/source/URLSource']
        |
        j2:jhove2/j2:feature[j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value[.='URLSource']]" 
        mode="struct_map">
               
        <mets:div TYPE="URLSource" xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="ID" select="concat('structMap_',generate-id())"/>
            <xsl:attribute name="LABEL" select="j2:features/j2:feature[@name='SourceName']/j2:value"/>
            <xsl:attribute name="ADMID"><xsl:value-of separator=" " select="concat('amdSec_',generate-id()),$GenAmdID"/></xsl:attribute>
            <mets:fptr xmlns:mets="http://www.loc.gov/METS/">
                <xsl:attribute name="FILEID">
                    <xsl:value-of select="concat('fileSec_',generate-id())"/>
                </xsl:attribute> 
            </mets:fptr>
        </mets:div>
    </xsl:template>
    
    
    <!-- FileSetSource in structMap -->
    <xsl:template 
        match="j2:jhove2/j2:feature[j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']/j2:value[.='FileSetSource']]" 
        mode="struct_map">
                
        <mets:div TYPE="FileSet" xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="ID" select="concat('structMap_',generate-id())"/>
            <xsl:apply-templates 
                select="j2:features/j2:feature[@name='ChildSources']/j2:features/j2:feature[@name='ChildSource']" mode="struct_map"/>
        </mets:div>
    </xsl:template>
    
    
    
    <xsl:template 
        match="j2:feature[@name='ChildSources']" mode="struct_map">
        <xsl:apply-templates select="j2:features/j2:feature[@name='ChildSource']" mode="struct_map"/>
    </xsl:template>

</xsl:stylesheet>
