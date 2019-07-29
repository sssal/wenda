<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UtF-8">
    <title>freemarker template</title>
</head>
<body>
<pre>
    ${value1}
    Hello home<br>
    ${value2!"vvv"}

    <!--    colors: $!{colors}-->
    <!--    #foreach($color in $colors)-->
    <!--        This is Color: $color, $!{color}-->
    <!--    #end-->
<#--    -->
    <#list colors as color>
        ${color_index}${color}
    </#list>
</pre>

</body>
</html>