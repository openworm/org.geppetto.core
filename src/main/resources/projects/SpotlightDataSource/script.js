var crossDomainTrue={VFB:{url:"http://vfbdev.inf.ed.ac.uk/search/select?fl=short_form,label,synonym,id,type,has_narrow_synonym_annotation,has_broad_synonym_annotation&start=0&fq=ontology_name:(fbbt)&fq=is_obsolete:false&fq=shortform_autosuggest:VFB_*%20OR%20shortform_autosuggest:FBbt_*&rows=250&bq=is_defining_ontology:true%5E100.0%20label_s:%22%22%5E2%20synonym_s:%22%22%20in_subset_annotation:BRAINNAME%5E3%20short_form:FBbt_00003982%5E2&q=$SEARCH_TERM$&defType=edismax&qf=label%20synonym%20label_autosuggest_ws%20label_autosuggest_e%20label_autosuggest%20synonym_autosuggest_ws%20synonym_autosuggest_e%20synonym_autosuggest%20shortform_autosuggest%20has_narrow_synonym_annotation%20has_broad_synonym_annotation&wt=json&indent=true",crossDomain:!0,id:"short_form",label:{field:"label",formatting:"$VALUE$ [$ID$]"},explode_ids:{field:"short_form",formatting:"$VALUE$ ($LABEL$)"},explode_synonyms:{field:"synonym",formatting:"$VALUE$ ($LABEL$)[$ID$]"},type:{class:{actions:["Model.getDatasources()[0].fetchVariable('$ID$', function(){});"],icon:"fa-dot-circle-o"},individual:{actions:["Model.getDatasources()[0].fetchVariable('$ID$', function(){ var instance = Instances.getInstance('$ID$'); resolve3D('$ID$', function(){instance.select(); GEPPETTO.Spotlight.openToInstance(instance);}); }); "],icon:"fa-square-o"}}}};GEPPETTO.Spotlight.addDataSource(crossDomainTrue);