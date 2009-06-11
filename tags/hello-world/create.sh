#!/bin/sh

cd ..

pax-create-project -g com.jolira.enzian -a enzian -v 0.0.1-SNAPSHOT

cd enzian

#pax-add-repository -i osp4j.releases -u http://repository.ops4j.org/maven2/
pax-add-repository -i osp4j.releases -u http://repository.ops4j.org/mvn-releases/
pax-add-repository -i ops4j.snapshots -u http://repository.ops4j.org/mvn-snapshots/

pax-import-bundle -g org.ops4j.pax.web               -a pax-web-bundle -v 0.6.0        -- -DwidenScope -DimportTransitive
pax-import-bundle -g org.ops4j                       -a peaberry       -v 1.1.1        -- -DwidenScope -DimportTransitive
pax-import-bundle -g org.ops4j.peaberry.dependencies -a aopalliance    -v 1.0-SNAPSHOT -- -DwidenScope -DimportTransitive
pax-import-bundle -g org.ops4j.peaberry.dependencies -a guice          -v 2.0-SNAPSHOT -- -DwidenScope -DimportTransitive
pax-import-bundle -g org.apache.wicket               -a wicket         -v 1.4-rc4      -- -DwidenScope -DimportTransitive
#pax-import-bundle -g org.apache.wicket              -a wicket-guice   -v 1.4-rc4      -- -DwidenScope -DimportTransitive
pax-import-bundle -g org.slf4j                       -a slf4j-api      -v 1.3.1        -- -DwidenScope -DimportTransitive
pax-import-bundle -g org.slf4j                       -a slf4j-simple   -v 1.3.1        -- -DwidenScope -DimportTransitive

# pax-wrap-jar    -g javax.servlet -a servlet-api -v 2.5    -- -DwrapTransitive "-DtargetDirectory=dependencies"

pax-create-bundle -p com.jolira.enzian.app -v 0.0.1-SNAPSHOT -- -DwrapTransitive "-DtargetDirectory=plugins"

./build.sh
