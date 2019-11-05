<html>
    <frameset cols="50%,50%">
        <#list backendConfigurations as bc>
            <frame src="${bc.proxyTo}/bigo_presto_monitor_ui/#" />
        </#list>
    </frameset>
</html>