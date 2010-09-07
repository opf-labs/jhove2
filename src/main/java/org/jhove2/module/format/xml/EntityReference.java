package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
  * A nested class to contain a single entity name and the count of its references in the XML document
  */
 public class EntityReference extends AbstractReportable  {
     
     /** The entity name. */
     protected String name;

     /** The entity reference count. */
     protected Integer count;
     
     /**
      * Instantiates a new entity reference.
      * 
      * @param name the name
      * @param count the initial count
      */
     public EntityReference(String name, Integer count){
         this.name = name;
         this.count = count;
     }
         
     /**
      * Gets the entity name.
      * 
      * @return the name
      */
     @ReportableProperty(order = 1, value = "Entity name")
     public String getName() {
        return name;
    }

     /**
      * Gets the count.
      * 
      * @return the count
      */
     @ReportableProperty(order = 1, value = "Reference count")
     /** Gets the entity reference count. */
    public Integer getCount() {
        return count;
    }         
     
 }