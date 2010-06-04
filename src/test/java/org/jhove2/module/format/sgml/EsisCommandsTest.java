package org.jhove2.module.format.sgml;

//import java.io.*;
//import java.util.*;
import org.antlr.runtime.*;
//import org.antlr.runtime.debug.DebugEventSocketProxy;



public class EsisCommandsTest{

	
    public static void main(String args[]) throws Exception {
    	ESISCommandsLexer lex = new ESISCommandsLexer(new ANTLRFileStream(
    			"C:\\cvs_repository\\TestData\\samplefiles\\portico\\workarea\\biobatch\\input\\file01.sgm.out", 
    			"UTF8"));
        CommonTokenStream tokens = new CommonTokenStream(lex);
        ESISCommandsParser g = new ESISCommandsParser(tokens);
        try {
        	g.esis();
        	printInfo(g);
        }
        catch (Exception e){
        	System.err.println(e.getMessage());
        }
        lex = new ESISCommandsLexer(new ANTLRFileStream(
    			"C:\\cvs_repository\\TestData\\samplefiles\\portico\\workarea\\biobatch\\input\\testfail.txt", 
    			"UTF8"));
        tokens = new CommonTokenStream(lex);
        g = new ESISCommandsParser(tokens);
        try {
        	g.esis();
        	System.out.println("Processing bad file");
        	printInfo(g);
        }
        catch (Exception e){
        	System.err.println(e.getMessage());
        }
    	System.out.println("DONE!!");
    }
    
    public static void printInfo(ESISCommandsParser g ){ 	
    	if (g.isSgmlValid){
    		System.out.println("EsisTest:  is valid");
    	}
    	else {
    		System.out.println("EsisTest:  is NOT valid");
    	}
    	
    	System.out.println("sdata element count: " + g.sDataCount);
//    	for (String sdataName:g.sdataNames){
//    		System.out.println(sdataName);
//    	}

    	System.out.println("publicIdCount = " + g.publicIdCount);
    	for (String pubid:g.pubIds){
    		System.out.println(pubid);
    	}
    	
    	System.out.println("File sysids count = " + g.fileNamesCount);
//    	for (String sysid:g.extEntFileNames){
//    		System.out.println(sysid);
//    	}
    	
    	System.out.println("External text entity count = " + g.fileNamesCount);
    	for (String entName:g.extTextEntNames){
    		System.out.println(entName);
    	}
    	
    	System.out.println("Internal data entity count = " + g.intDataEntCount);
//    	for (Map.Entry<String, String> entry : g.intEnt2Value.entrySet()){
//    		String entName = entry.getKey();
//    		String entValue = entry.getValue();
//    		System.out.println("Entity " + entName + " value = " + entValue);
//    	}
//    	for (Map.Entry<String, String> entry : g.intEnt2Type.entrySet()){
//    		String entName = entry.getKey();
//    		String entValue = entry.getValue();
//    		System.out.println("Entity " + entName + " type = " + entValue);
//    	}
    }
}