[#-- Assigns: Get Content --]
[#--[#assign extension = ctx.getAggregationState().getExtension()!]--]

[#-- Create XML --]
[#--[#if extension = "xml"]--]
[#--[#include "/nfl/pages/mainXml.ftl" ]--]
[#--[/#if]--]
<html>
    <head>
        <style>
            p {
                margin: 0;
            }
            .paragraph-title-wrapper {
                width: 610px;
                height: 80px;
                border: 1px dotted #a6a6a6;
                position: relative;
            }

            .paragraph-logo {
                text-align: center;
                line-height: 50px;
                width: 50px;
                height: 50px;
                border: 2px solid #a6a6a6;
                position: absolute;
                top: 15px;
                left: 10px;
                font-size: 26px;
            }

            .paragraph-title-text {
                position: absolute;
                top: 30px;
                left: 70px;
            }

            .ax-shape {
                font-family: 'ArialMT', 'Arial';
                font-weight: 400;
                font-style: normal;
                font-size: 13px;
                color: #333333;
                text-align: center;
                line-height: normal;
            }

            .ax_paragraph {
                font-family: 'ArialMT', 'Arial';
                font-weight: 400;
                font-style: normal;
                font-size: 13px;
                color: #333333;
                text-align: left;
                line-height: normal;
            }
        </style>
    </head>
<body>
    <div>
        [#assign sections = model.sections!]
        [#assign hasSections = sections?has_content]

        [#if hasSections]
            [#list sections as section]
                <div class="paragraph-title-wrapper">
                    <div class="paragraph-logo">&sect;</div>
                    <span class="paragraph-title-text">${section.title}</span>
                </div>
                <div class="ax_paragraph">${section.text}</div>
                <img src="${section.image}" height="100" width="385"/>
            [/#list]
        [#else]
            <p>The article is currently empty</p>
        [/#if]
    </div>
</body>
</html>

