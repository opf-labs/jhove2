JHOVE2 -- Next-generation architecture for format-aware characterization

Please refer to the JHOVE2 User’s Guide for procedures on installing, 
configuring, and running JHOVE2.

JHOVE2 is a collaborative project of the California Digital Library, Portico,
and Stanford University.  The goals of the project are: 

 * To refactor the existing JHOVE architecture and API in order to:
    o Rectify known inefficiencies and idiosyncrasies of design and
      implementation
    o Simplify the process of integrating JHOVE2 technology into other
      systems, services, and workflows
    o Encourage third-party extensions to the baseline JHOVE2
      functionality

 * To support enhancements to existing JHOVE functionality, including:
    o Separation of identity detection from validation
    o A more sophisticated digital object model explicitly supporting complex
      multi-file or hierarchical objects
    o A more generic plug-in architecture
    o Standardized error and profile handling
    o A rules-based assessment of characterization information in the context
      of local policies

 * To develop JHOVE2 modules supporting a number of important preservation-
   related processes.
      
   Digital preservation is the set of intentions, strategies, and activities
   aimed at ensuring the continuing usability of digital objects over time.
   However, since digital objects rely on explicit technological mediation
   in order to be useful, they are inherently fragile with respect to
   technological change.  Over any significant time period, a gap inevitably
   arises in the ability of a digital object to function in contemporaneous
   technological contexts.  Put most simply, digital preservation is concerned
   with effectively managing the consequences of this gap, which is achievable
   only to the extent to which the gap is quantifiable.  The necessary
   quantification comes, in part, from characterization.
      
   Characterization exposes the significant properties of a digital object and
   provides a stable starting point for iterative preservation planning and
   action.  The JHOVE2 project defines characterization in terms of four
   aspects:

    o Identification.  Identification is the process of determining the
      presumptive format of a digital object on the basis of suggestive
      extrinsic hints (for example, an HTTP Content-type header) and intrinsic
      signatures, both internal (a magic number) and external (a file
      extension).  Ideally, format identification should be reported in terms
      of a level of confidence.

    o Validation.  Validation is the process of determining a digital object's
      level of conformance to the requirements of its presumptive format.
      These requirements are expressed by the normative syntactic and semantic
      rules of that format's authoritative specification. Ideally, the
      determination of conformance should be based on commonly accepted
      objective criteria.  However, many format specifications - particularly 
      those not created as part of explicit standardization efforts - suffer
      from ambiguous language requiring subjective interpretation.  The
      incorporation of such interpretative decisions into automated systems
      should be highly configurable to support local variation of preservation
      policy and practice.

    o Feature extraction.  Feature extraction is the process of reporting the
      intrinsic properties of a digital object significant to preservation
      planning and action.  These features can function in many contexts as a
      surrogate for the object itself for purposes of evaluation and decision
      making.  Note that since digital preservation is concerned with planning
      for future activities, potentially in response to unforeseeable
      circumstances, predicting which properties will one day be significant
      can be problematic.  Prudence therefore suggests reporting the most
      inclusive set of properties possible, while providing sufficiently fine
      granularity of control to allow for appropriate localized configuration.

    o Assessment.  Assessment is the process of determining the level of
      acceptability of a digital object for a specific use on the basis of
      locally-defined policies. Assessments can be used to select appropriate
      processing actions.  In a repository ingest workflow, for example, the
      range of possible actions could include rejection, normalization, or
      acceptance in original form.

      Reduced to simpler terms, characterization answers the following
      questions relevant to the preservation of a digital object:

       - What is it?
       - What is it really?
       - What are its salient characteristics?
       - What should be done with it?

      Or, more reductively, What? and So What?

The JHOVE2 project is funded by the Library of Congress under its National
Digital Information Infrastructure Preservation Program (NDIIPP).

All JHOVE2 software deliverables are available under the terms of the open
source BSD license.

The JHOVE2 project team is:

California Digital Library
 * Stephen Abrams
 * Patricia Cruse
 * John Kunze
 * Marisa Strong
 * Perry Willett

Portico
 * John Meyer
 * Sheila Morrissey

Stanford University
 * Richard Anderson
 * Tom Cramer
 * Hannah Frost

Library of Congress
 * Martha Anderson
 * Justin Littman

With help from
 * Walter Henry
 * Nancy Hoebelheinrich
 * Keith Johnson
 * Even Owens
 * Isaac Rabinovitch

More information about JHOVE2 and the JHOVE2 project is available at:

<http://jhove2.org/>

<mailto:JHOVE2-Announce-l@listserv.ucop.edu>
<mailto:JHOVE2-Techtalk-l@listserv.ucop.edu>


