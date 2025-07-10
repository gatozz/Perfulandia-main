## Dado que Reporte es una clase abstracta, no crearás directamente instancias de ella. En su lugar, crearás instancias de las subclases: ReporteInventario, ReporteVenta y ReporteRendimiento.

## post http://localhost:8080/api/reportes/inventario 
{
    "fechaCreacion": "2024-07-24",
    "alertaBajoStock": true
}
## put http://localhost:8080/api/reportes/inventario/1
{
    "fechaCreacion": "2024-07-25",
    "alertaBajoStock": false
}

## POST - Crear un nuevo reporte de venta:
## /api/reportes/venta
{
    "fechaCreacion": "2024-07-24"
}
## PUT - Actualizar un reporte de venta existente (asumiendo que el ID del reporte a actualizar es 2):
## Ruta: /api/reportes/venta/2  (Reemplaza '2' con el ID real)
{
    "fechaCreacion": "2024-07-26"
}
## POST - Crear un nuevo reporte de rendimiento:
## Ruta: /api/reportes/rendimiento
{
    "fechaCreacion": "2024-07-24"
}
## PUT - Actualizar un reporte de rendimiento existente (asumiendo que el ID del reporte a actualizar es 3)
## Ruta: /api/reportes/rendimiento/3  (Reemplaza '3' con el ID real)
{
    "fechaCreacion": "2024-07-27"
}
## GET - Obtener todos los reportes de inventario:
## Ruta: /api/reportes/inventario

## GET - Obtener un reporte de inventario por ID (ejemplo con ID 1):
## Ruta: /api/reportes/inventario/1 (Reemplaza '1' con el ID real)

## DELETE - Eliminar un reporte de inventario por ID (ejemplo con ID 1)
## Ruta: /api/reportes/inventario/1 (Reemplaza '1' con el ID real)