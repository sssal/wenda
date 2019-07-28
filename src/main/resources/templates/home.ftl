<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UtF-8">
    <title>freemarker template</title>
</head>
<body>
<pre>
    ${value1}
    Hello home
    ${value2}!

    <!--    colors: $!{colors}-->
    <!--    #foreach($color in $colors)-->
    <!--        This is Color: $color, $!{color}-->
    <!--    #end-->
<#--    -->
    <#list colors as color>
        ${color}
    </#list>
</pre>

</body>
</html>