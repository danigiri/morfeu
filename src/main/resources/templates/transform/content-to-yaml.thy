---
[# th:each="cell : ${v.cells.asList()[0].asComplex().children().asList()}"]/*[-
-]*/[# th:replace="templates/transform/cell-to-yaml.thy (cell=${cell},case=${'obj-to-yaml'},indent=${''})"][/]/*[-
-]*/[/]