<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs j2 xd"
    xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
    xmlns:j2="http://jhove2.org/xsd/1.0.0" 
    version="2.0">
    <xd:doc scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Aug 26, 2010</xd:p>
            <xd:p><xd:b>Author:</xd:b> mstoef</xd:p>
            <xd:p>This is an amdSec module for the jhove2_to_mets-v1 script</xd:p>
        </xd:desc>
    </xd:doc>
    
  
    
    <!-- create a temporary tree that we can query for construction of 
        the amdSec's for the doc as a whole and for the root source node  -->
    <xsl:variable name="rootModules">
        <mets:rootModules xmlns:mets="http://www.loc.gov/METS/">
            <xsl:copy-of select="/j2:jhove2/j2:feature/j2:features/j2:feature[@name='Modules']"/>
        </mets:rootModules>
    </xsl:variable>
    
    <!-- path of the 'childsources' tree -->
    <xsl:variable 
        name="childSourcesTree" 
        select="/j2:jhove2/j2:feature/j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/ChildSources']"/>
    
    <xsl:template name="make_amdSec">
        <!-- our current context is /j2:jhove2 -->
        <xsl:for-each select="j2:feature">
            
            <!-- This is the generic amdSec -->
            <mets:amdSec xmlns:mets="http://www.loc.gov/METS/" ID="{$GenAmdID}">
                <xsl:if test="key('GetModbyRName','JHOVE2CommandLine',$rootModules)">
                    <xsl:call-template name="make_techMD">
                        <xsl:with-param name="MDNode" select="key('GetModbyRName','JHOVE2CommandLine',$rootModules)"/>
                    </xsl:call-template>
                </xsl:if>
                <xsl:if test="key('GetModbyRName','JHOVE2',$rootModules)">
                    <xsl:call-template name="make_techMD">
                        <xsl:with-param name="MDNode" select="key('GetModbyRName','JHOVE2',$rootModules)"/>
                    </xsl:call-template>
                </xsl:if>
                <xsl:for-each select="key('GetModbyScope','Generic',$rootModules)
                    [not(descendant::j2:feature[@name='ReportableName']/j2:value[matches(.,'^(JHOVE2|JHOVE2CommandLine)$')])]">
                    <xsl:call-template name="make_techMD">
                        <xsl:with-param name="MDNode" select="."/>
                    </xsl:call-template>
                </xsl:for-each>
                <xsl:for-each 
                    select="$childSourcesTree/descendant::j2:feature[@name='Module'][j2:features/j2:feature[@name='Scope']/j2:value[.='Generic']]">
                    <xsl:call-template name="make_techMD">
                        <xsl:with-param name="MDNode" select="."/>
                    </xsl:call-template>
                </xsl:for-each>
            </mets:amdSec>
            
            <!-- this is the root source amdSec -->
            <mets:amdSec xmlns:mets="http://www.loc.gov/METS/">
                <xsl:attribute name="ID"><xsl:value-of select="concat('amdSec_',generate-id())"/></xsl:attribute>
                <!-- Grab Modules that are of 'Specific' scope -->
                <xsl:for-each select="$rootModules/descendant::j2:feature[@name='Module']
                    [not(j2:features/j2:feature[@name='Scope']/j2:value[.='Generic']) 
                    and 
                    not(descendant::j2:feature[@name='ReportableName']/j2:value[matches(.,'^(JHOVE2|JHOVE2CommandLine)$')])]">
                    <xsl:call-template name="make_techMD">
                        <xsl:with-param name="MDNode" select="."/>
                    </xsl:call-template>
                </xsl:for-each>
                
                <xsl:call-template name="select_other_techMD"/>
                
            </mets:amdSec>
        </xsl:for-each>
        
        <!-- pull each individual ChildSource and generate an amdSec -->
        <xsl:apply-templates
            select="descendant::j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/ChildSources/ChildSource']"
            mode="amd_sec"/>
    </xsl:template>
    
    
    
    <xsl:template name="make_techMD">
        <xsl:param name="MDNode"/>
        <mets:techMD xmlns:mets="http://www.loc.gov/METS/">
            <xsl:attribute name="ID"><xsl:value-of select="concat('jhove2Output_',generate-id($MDNode))"/></xsl:attribute>
            <xsl:attribute name="CREATED"><xsl:value-of select="$jhoveDateTime"/></xsl:attribute>
            <mets:mdWrap xmlns:mets="http://www.loc.gov/METS/">
                <xsl:attribute name="MDTYPE">OTHER</xsl:attribute>
                <xsl:attribute name="OTHERMDTYPE">JHOVE2</xsl:attribute>
                <xsl:attribute name="MDTYPEVERSION">jhove2_tfx_v1.0</xsl:attribute>
                <mets:xmlData xmlns:mets="http://www.loc.gov/METS/">
                    <xsl:copy-of select="$MDNode"/>
                </mets:xmlData>
            </mets:mdWrap>
        </mets:techMD>
    </xsl:template>
    
    
  
    <xsl:template name="select_other_techMD">
        <!-- j2:features/j2:feature fid="http://jhove2.org/terms/property/org/jhove2/core/source/Source/PresumptiveFormats" -->
        <xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/PresumptiveFormats']">
            <xsl:call-template name="make_techMD">
                <xsl:with-param name="MDNode" 
                    select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/PresumptiveFormats']"/>
            </xsl:call-template>
        </xsl:if>
        
        <!-- j2:features/j2:feature fid="http://jhove2.org/terms/property/org/jhove2/core/source/Source/TimerInfo" -->
        <xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/TimerInfo']">
            <xsl:call-template name="make_techMD">
                <xsl:with-param name="MDNode"
                    select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/TimerInfo']"/>
            </xsl:call-template>
        </xsl:if>
        
        <!-- j2:features/j2:feature fid="http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName" -->
        <xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']">
            <xsl:call-template name="make_techMD">
                <xsl:with-param name="MDNode" 
                    select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/reportable/AbstractReportable/ReportableName']"/>
            </xsl:call-template>
        </xsl:if>
        
        
        <!-- j2:features/j2:feature fid="http://jhove2.org/terms/property/org/jhove2/core/source/Source/NumChildSources" -->
        <xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/NumChildSources']">
            <xsl:call-template name="make_techMD">
                <xsl:with-param name="MDNode" 
                    select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/NumChildSources']"/>
            </xsl:call-template>
        </xsl:if>
        
        <!-- j2:features/j2:feature[@fid='http://jhove2.org/terms/message/org/jhove2/core/source/Source/Messages'] -->
        <xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/message/org/jhove2/core/source/Source/Messages']">
            <xsl:call-template name="make_techMD">
                <xsl:with-param name="MDNode" 
                    select="j2:features/j2:feature[@fid='http://jhove2.org/terms/message/org/jhove2/core/source/Source/Messages']"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
    
    
    <!-- ChildSource in amdSec -->
    <xsl:template 
        match="j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/ChildSources/ChildSource']" 
        mode="amd_sec">
        <mets:amdSec xmlns:mets="http://www.loc.gov/METS/">
              <xsl:attribute name="ID"><xsl:value-of select="concat('amdSec_',generate-id())"/></xsl:attribute>
            
            <!-- Grab all Modules -->
            <!-- j2:features/j2:feature fid="http://jhove2.org/terms/property/org/jhove2/core/source/Source/Modules" -->
            <xsl:if test="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/Modules']">
                <xsl:call-template name="make_techMD">
                    <xsl:with-param name="MDNode" 
                        select="j2:features/j2:feature[@fid='http://jhove2.org/terms/property/org/jhove2/core/source/Source/Modules']"/>
                </xsl:call-template>
            </xsl:if>

            <!-- Grab other tech md -->
            <xsl:call-template name="select_other_techMD"/>
        </mets:amdSec>
    </xsl:template>
    
</xsl:stylesheet>
