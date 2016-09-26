<?xml version="1.0" encoding="ASCII"?>
<gep:GeppettoModel
    xmi:version="2.0"
    xmlns:xmi="http://www.omg.org/XMI"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:gep="https://raw.githubusercontent.com/openworm/org.geppetto.model/query/src/main/resources/geppettoModel.ecore"
    xmlns:gep_1="https://raw.githubusercontent.com/openworm/org.geppetto.model/query/src/main/resources/geppettoModel.ecore#//types"
    xmlns:gep_2="https://raw.githubusercontent.com/openworm/org.geppetto.model/query/src/main/resources/geppettoModel.ecore#//datasources">
  <libraries
      id="SWCLibrary"
      name="SWC"/>
  <libraries
      id="OBJLibrary"
      name="OBJ"/>
  <libraries
      id="OWLLibrary"
      name="OWL"/>
  <libraries
      id="ontology"
      name="Ontology">
    <types xsi:type="gep_1:SimpleType"
        id="Individual"
        name="Individual"/>
    <types xsi:type="gep_1:SimpleType"
        id="Class"
        name="Class"/>
    <types xsi:type="gep_1:SimpleType"
        id="Neuron"
        name="Neuron"/>
    <types xsi:type="gep_1:SimpleType"
        id="Tract"
        name="Tract"/>
    <types xsi:type="gep_1:SimpleType"
        id="Clone"
        name="Clone"/>
    <types xsi:type="gep_1:SimpleType"
        id="Synaptic_neuropil"
        name="Synaptic Neuropil"/>
    <types xsi:type="gep_1:SimpleType"
        id="VFB"
        name="Virtual Fly Brain"/>
    <types xsi:type="gep_1:SimpleType"
        id="Orphan"
        name="No Meta Data"/>
    <types xsi:type="gep_1:SimpleType"
        id="Obsolete"
        name="Obsolete"/>
    <types xsi:type="gep_1:SimpleType"
        id="Synaptic_neuropil_domain"
        name="Synaptic Neuropil Domain"/>
    <types xsi:type="gep_1:SimpleType"
        id="Synaptic_neuropil_subdomain"
        name="Synaptic Neuropil Subdomain"/>
    <types xsi:type="gep_1:SimpleType"
        id="Synaptic_neuropil_block"
        name="Synaptic Neuropil Block"/>
    <types xsi:type="gep_1:SimpleType"
        id="FBDV"
        name="FlyBase Development CV"/>
    <types xsi:type="gep_1:SimpleType"
        id="FBCV"
        name="FlyBase Controlled Vocabulary"/>
    <types xsi:type="gep_1:SimpleType"
        id="FBBI"
        name="FlyBase Biological Imaging Methods"/>
    <types xsi:type="gep_1:SimpleType"
        id="Root"
        name="Top Object"/>
    <types xsi:type="gep_1:SimpleType"
        id="pub"
        name="Publication"/>
    <types xsi:type="gep_1:SimpleType"
        id="Resource"
        name="Resource"/>
    <types xsi:type="gep_1:SimpleType"
        id="VFB_00017894"
        name="JFRC2 template"/>
    <types xsi:type="gep_1:SimpleType"
        id="VFB_00030786"
        name="BrainName standard - Ito half brain"/>
  </libraries>
  <libraries
      id="vfbLibrary"
      name="VFB"/>
  <dataSources
      id="neo4JDataSourceService"
      name="neo4j Data Source"
      dataSourceService="neo4jDataSource"
      url="http://vfbdev.inf.ed.ac.uk/neo4jdb/data/transaction"
      dependenciesLibrary="//@libraries.3"
      targetLibrary="//@libraries.4">
    <libraryConfigurations
        library="//@libraries.0"
        modelInterpreterId="swcModelInterpreterService"
        format="swc"/>
    <libraryConfigurations
        library="//@libraries.1"
        modelInterpreterId="objModelInterpreterService"
        format="obj"/>
    <libraryConfigurations
        library="//@libraries.2"
        modelInterpreterId="owlModelInterpreterService"
        format="owl"/>
    <queries
        xsi:type="gep_2:CompoundQuery"
        name="Get and process images from Neo4j"
        description="">
      <queryChain
          xsi:type="gep_2:SimpleQuery"
          name="Get images from Neo4j"
          description="fetch Individual instances from ID list"
          query="MATCH (n:VFB:Class)&lt;-[:SUBCLASSOF|INSTANCEOF*..]-(i:Individual)-[:Related { label : 'depicts' } ]-(j:Individual)-[:Related { label : 'has_signal_channel' } ]-(k:Individual)-[:Related { label: 'has_background_channel' } ]-(m:Individual) WHERE n.short_form IN $ARRAY_ID_RESULTS RETURN n.short_form as class_Id, COLLECT (DISTINCT { image_name: i.label, image_id: i.short_form, image_thumb: 'http://www.virtualflybrain.org/data/'+substring(k.short_form,0,3)+'/'+substring(k.short_form,3,1)+'/'+substring(k.short_form,5,4)+'/'+substring(k.short_form,9,4)+'/thumbnail.png', template_id: m.short_form}) AS inds"
          countQuery="MATCH (n:VFB:Class)&lt;-[:SUBCLASSOF|INSTANCEOF*..]-(i:Individual) WHERE n.short_form IN $ARRAY_ID_RESULTS RETURN count(i) AS count"/>
      <queryChain
          xsi:type="gep_2:ProcessQuery"
          name="Process images"
          queryProcessorId="vfbCreateImagesForQueryResultsQueryProcessor"/>
    </queries>
    <fetchVariableQuery
        xsi:type="gep_2:CompoundQuery"
        name="The compound query for augmenting a type"
        description="">
      <queryChain
          xsi:type="gep_2:SimpleQuery"
          name="Get id/name/superTypes/description/comment"
          description="Fetches essential details."
          query="MATCH (n { short_form: '$ID' } ) RETURN n.label as name, n.short_form as id, n.description as description, n.`annotation-comment` as comment, labels(n) as supertypes LIMIT 1"
          countQuery="MATCH (n { short_form: '$ID' } ) RETURN count(n) as count"/>
      <queryChain
          xsi:type="gep_2:ProcessQuery"
          name="This processing step will populate a Variable with the superType resulting from the previous query"
          description=""
          queryProcessorId="vfbTypesQueryProcessor"/>
      <queryChain
          xsi:type="gep_2:SimpleQuery"
          name="Fetch relationships and references for Class"
          description="Pull all relationships and references"
          query="MATCH (n:VFB:Class { short_form: '$ID' } )-[r:SUBCLASSOF|Related|has_reference]->(sc) RETURN r as relationship, sc.label as relName, sc.short_form as relId, sc.miniref as relRef, sc.FlyBase as relFBrf, sc.PMID as relPMID, sc.DOI as relDOI, sc.http as relLink"
          countQuery="MATCH (n:VFB:Class { short_form: '$ID' } )-[r:SUBCLASSOF|Related|has_reference]->(sc) RETURN count(*) as count">
        <matchingCriteria
            type="//@libraries.3/@types.1"/>
      </queryChain>
      <queryChain
          xsi:type="gep_2:ProcessQuery"
          name="This processing step will populate a Variable with the synonyms and references resulting from the previous query"
          description="This processing step will populate a Variable with the synonyms and references resulting from the previous query"
          queryProcessorId="vfbImportTypesSynonymQueryProcessor">
        <matchingCriteria
            type="//@libraries.3/@types.1"/>
      </queryChain>
      <queryChain
          xsi:type="gep_2:SimpleQuery"
          name="Fetch related and references for individuals"
          description="Fetch related and references for individuals"
          query="MATCH (n:VFB:Individual { short_form: '$ID' } )-[r:INSTANCEOF|Related|has_reference]->(sc) RETURN r as relationship, sc.label as relName, sc.short_form as relId, sc.miniref as relRef, sc.FlyBase as relFBrf, sc.PMID as relPMID, sc.DOI as relDOI, sc.http as relLink"
          countQuery="MATCH (n:VFB:Individual { short_form: '$ID' } )-[r:INSTANCEOF|Related|has_reference]->(sc) RETURN count(n) as count">
        <matchingCriteria
            type="//@libraries.3/@types.0"/>
      </queryChain>
      <queryChain
          xsi:type="gep_2:ProcessQuery"
          name="This processing step will populate a Variable with the related and references resulting from the previous query"
          description="This processing step will populate a Variable with the related and references resulting from the previous query"
          queryProcessorId="vfbImportTypesSynonymQueryProcessor">
        <matchingCriteria
            type="//@libraries.3/@types.0"/>
      </queryChain>
      <queryChain
          xsi:type="gep_2:SimpleQuery"
          name="Fetch 6 example individuals for Class"
          description="Fetch up to 6 example Individual instances of this Class or subclasses"
          query="MATCH (n:Class { short_form: '$ID' } )&lt;-[r:SUBCLASSOF|INSTANCEOF*..]-(i:Individual)&lt;-[:Related{label:'depicts'}]-(c:Individual)&lt;-[:Related{label:'has_signal_channel'}]-(im:Individual)-[:Related{label:'has_background_channel'}]->(t:Template) RETURN i.short_form as exId, i.label as exName, substring(im.short_form,0,3)+'/'+substring(im.short_form,3,1)+'/'+substring(im.short_form,5,4)+'/'+substring(im.short_form,9,4)+'/thumbnail.png' as exThumb, t.short_form as exTemp LIMIT 12"
          countQuery="MATCH (n:VFB:Class { short_form: '$ID' } )&lt;-[r:SUBCLASSOF|INSTANCEOF*..]-(i:Individual) RETURN count(i) as count">
        <matchingCriteria
            type="//@libraries.3/@types.1"/>
      </queryChain>
      <queryChain
          xsi:type="gep_2:ProcessQuery"
          name="Add Example thumbnails"
          description="Add example thumbnails for Individual instances of this Class or subclasses"
          queryProcessorId="vfbImportTypesQueryProcessor">
        <matchingCriteria
            type="//@libraries.3/@types.1"/>
      </queryChain>
      <queryChain
          xsi:type="gep_2:SimpleQuery"
          name="Image Folder and Template"
          description="Fetch the image folder and template details"
          query="MATCH (i:Individual { short_form: '$ID' } )&lt;-[:Related{label:'depicts'}]-(c:Individual)&lt;-[:Related{label:'has_signal_channel'}]-(im:Individual)-[:Related{label:'has_background_channel'}]->(t:Template) RETURN substring(im.short_form,0,3)+'/'+substring(im.short_form,3,1)+'/'+substring(im.short_form,5,4)+'/'+substring(im.short_form,9,4)+'/' as imageDir, t.short_form as tempId, t.label as tempName"
          countQuery="MATCH (i:Individual { short_form: '$ID' } )&lt;-[:Related{label:'depicts'}]-(c:Individual)&lt;-[:Related{label:'has_signal_channel'}]-(im:Individual)-[:Related{label:'has_background_channel'}]->(t:Template) RETURN count(t) as count">
        <matchingCriteria
            type="//@libraries.3/@types.0"/>
      </queryChain>
      <queryChain
          xsi:type="gep_2:ProcessQuery"
          name="Add Thumbnail for VFB Individuals"
          description="Add Thumbnail for VFB Individuals"
          queryProcessorId="vfbImportTypesThumbnailQueryProcessor">
        <matchingCriteria
            type="//@libraries.3/@types.0"/>
      </queryChain>
      <queryChain
          xsi:type="gep_2:ProcessQuery"
          name="External Links"
          description="Add External Links for Classes"
          queryProcessorId="vfbImportTypesExtLinkQueryProcessor">
        <matchingCriteria
            type="//@libraries.3/@types.1"/>
      </queryChain>
      <queryChain
          xsi:type="gep_2:SimpleQuery"
          name="Ind References"
          description="References for Individual"
          query="MATCH (n:Class { short_form: '$ID' } )-[r:INSTANCEOF|Related|has_reference]->(sc) where sc.miniref is not null RETURN distinct '&lt;b>' + split(sc.miniref,',')[0] + ' (' + sc.year + ')&lt;/b> ' + sc.title + ' ' + split(sc.miniref,',')[2] + '. (' + coalesce('&lt;a href=\\'http://flybase.org/reports/' + sc.FlyBase + '\\' target=\\'_blank\\'>FlyBase:' + sc.FlyBase + '&lt;/a>; ', '') + coalesce('&lt;a href=\\'http://www.ncbi.nlm.nih.gov/pubmed/' + sc.PMID + '\\' target=\\'_blank\\'>PMID:' + sc.PMID + '&lt;/a>; ', '') + coalesce('&lt;a href=\\'http://dx.doi.org/' + sc.DOI + '\\' target=\\'_blank\\'>doi:' + sc.DOI + '&lt;/a>)', ')') as bib ORDER BY bib ASC"
          countQuery="MATCH (n:Class { short_form: '$ID' } )-[r:INSTANCEOF|Related|has_reference]->(sc) where sc.miniref is not null RETURN count(distinct sc) as count">
        <matchingCriteria
            type="//@libraries.3/@types.0"/>
      </queryChain>
      <queryChain
          xsi:type="gep_2:SimpleQuery"
          name="Class References"
          description="References for Class"
          query="MATCH (n:Individual { short_form: '$ID' } )-[r:SUBCLASSOF|Related|has_reference]->(sc) where sc.miniref is not null RETURN distinct '&lt;b>' + split(sc.miniref,',')[0] + ' (' + sc.year + ')&lt;/b> ' + sc.title + ' ' + split(sc.miniref,',')[2] + '. (' + coalesce('&lt;a href=\\'http://flybase.org/reports/' + sc.FlyBase + '\\' target=\\'_blank\\'>FlyBase:' + sc.FlyBase + '&lt;/a>; ', '') + coalesce('&lt;a href=\\'http://www.ncbi.nlm.nih.gov/pubmed/' + sc.PMID + '\\' target=\\'_blank\\'>PMID:' + sc.PMID + '&lt;/a>; ', '') + coalesce('&lt;a href=\\'http://dx.doi.org/' + sc.DOI + '\\' target=\\'_blank\\'>doi:' + sc.DOI + '&lt;/a>)', ')') as bib ORDER BY bib ASC"
          countQuery="MATCH (n:Individual { short_form: '$ID' } )-[r:SUBCLASSOF|Related|has_reference]->(sc) where sc.miniref is not null RETURN count(distinct sc) as count">
        <matchingCriteria
            type="//@libraries.3/@types.1"/>
      </queryChain>
      <queryChain
          xsi:type="gep_2:ProcessQuery"
          name="External Links"
          description="Add References for either Class of Ind"
          queryProcessorId="vfbImportTypesRefsQueryProcessor">
        <matchingCriteria
            type="//@libraries.3/@types.0"/>
        <matchingCriteria
            type="//@libraries.3/@types.1"/>
      </queryChain>
    </fetchVariableQuery>
  </dataSources>
  <dataSources
      id="aberOWLDataSource"
      name="Aber OWL Data Source"
      dataSourceService="aberOWLDataSource"
      url="http://vfbdev.inf.ed.ac.uk/aberowl/api/runQuery.groovy"
      dependenciesLibrary="//@libraries.3"
      targetLibrary="//@libraries.4">
    <queries
        xsi:type="gep_2:ProcessQuery"
        name="Retains id, name and definition"
        queryProcessorId="vfbAberOWLQueryProcessor"/>
    <queries
        xsi:type="gep_2:SimpleQuery"
        name="Part of"
        description="Part of"
        query="type=subeq&amp;query=%3Chttp://purl.obolibrary.org/obo/BFO_0000050%3E%20some%20%3Chttp://purl.obolibrary.org/obo/$ID%3E&amp;ontology=VFB"
        countQuery="">
      <matchingCriteria
          type="//@libraries.3/@types.1"/>
    </queries>
    <queries
        xsi:type="gep_2:SimpleQuery"
        name="Neurons"
        description="Neurons with some part here"
        query="type=subeq&amp;query=%3Chttp://purl.obolibrary.org/obo/FBbt_00005106%3E%20that%20%3Chttp://purl.obolibrary.org/obo/RO_0002131%3E%20some%20%3Chttp://purl.obolibrary.org/obo/$ID%3E&amp;ontology=VFB"
        countQuery="">
      <matchingCriteria
          type="//@libraries.3/@types.1 //@libraries.3/@types.5"/>
    </queries>
    <queries
        xsi:type="gep_2:SimpleQuery"
        name="Neurons Synaptic"
        description="Neurons with synaptic terminals here"
        query="type=subeq&amp;query=%3Chttp://purl.obolibrary.org/obo/FBbt_00005106%3E%20that%20%3Chttp://purl.obolibrary.org/obo/RO_0002130%3E%20some%20%3Chttp://purl.obolibrary.org/obo/$ID%3E&amp;ontology=VFB"
        countQuery="">
      <matchingCriteria
          type="//@libraries.3/@types.1 //@libraries.3/@types.5"/>
    </queries>
    <queries
        xsi:type="gep_2:SimpleQuery"
        name="Neurons Presynaptic"
        description="Neurons with presynaptic terminals here"
        query="type=subeq&amp;query=%3Chttp://purl.obolibrary.org/obo/FBbt_00005106%3E%20that%20%3Chttp://purl.obolibrary.org/obo/RO_0002113%3E%20some%20%3Chttp://purl.obolibrary.org/obo/$ID%3E&amp;ontology=VFB"
        countQuery="">
      <matchingCriteria
          type="//@libraries.3/@types.1 //@libraries.3/@types.5"/>
    </queries>
    <queries
        xsi:type="gep_2:SimpleQuery"
        name="Neurons Postsynaptic"
        description="Neurons with postsynaptic terminals here"
        query="type=subeq&amp;query=%3Chttp://purl.obolibrary.org/obo/FBbt_00005106%3E%20that%20%3Chttp://purl.obolibrary.org/obo/RO_0002110%3E%20some%20%3Chttp://purl.obolibrary.org/obo/$ID%3E&amp;ontology=VFB"
        countQuery="">
      <matchingCriteria
          type="//@libraries.3/@types.1 //@libraries.3/@types.5"/>
    </queries>
  </dataSources>
  <queries xsi:type="gep_2:CompoundRefQuery"
      id="neuronsparthere"
      name="Part of"
      description="Neurons with some part here"
      queryChain="//@dataSources.1/@queries.1 //@dataSources.1/@queries.0 //@dataSources.0/@queries.0">
    <matchingCriteria
        type="//@libraries.3/@types.1"/>
  </queries>
  <queries xsi:type="gep_2:CompoundRefQuery"
      id="neuronssynaptic"
      name="Neurons Synaptic"
      description="Neurons with synaptic terminals here"
      queryChain="//@dataSources.1/@queries.3 //@dataSources.1/@queries.0 //@dataSources.0/@queries.0">
    <matchingCriteria
        type="//@libraries.3/@types.1"/>
  </queries>
  <queries xsi:type="gep_2:CompoundRefQuery"
      id="neuronspresynaptic"
      name="Neurons Presynaptic"
      description="Neurons with presynaptic terminals here"
      queryChain="//@dataSources.1/@queries.4 //@dataSources.1/@queries.0 //@dataSources.0/@queries.0">
    <matchingCriteria
        type="//@libraries.3/@types.1"/>
  </queries>
  <queries xsi:type="gep_2:CompoundRefQuery"
      id="neuronspostsynaptic"
      name="Neurons Postsynaptic"
      description="Neurons with postsynaptic terminals here"
      queryChain="//@dataSources.1/@queries.5 //@dataSources.1/@queries.0 //@dataSources.0/@queries.0">
    <matchingCriteria
        type="//@libraries.3/@types.1"/>
  </queries>
</gep:GeppettoModel>
