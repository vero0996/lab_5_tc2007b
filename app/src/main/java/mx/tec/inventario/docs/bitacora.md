Verónica Zapata A01199193

Ejercicio 0:
Los miembros id, nombre, precio y cantidad sí van como columnas en la tabla porque representan hechos primarios y datos capturados directamente que no se pueden deducir a partir de ninguna otra propiedad, mientras que agotado y valorEnInventario no llevan columna por ser valores calculados (derivados mediante código a partir del precio y la cantidad), los cuales generarían redundancia innecesaria e incoherencias en la base de datos si alguno de los valores originales llegara a cambiar.

Ejercicio B1:
Existen cuatro llamadas a recargar() (dos en LaunchedEffect dentro del NavHost, una en el init de DetalleViewModel y otra al final de venderUno()); si se borra la del detalle y se edita un producto, al volver la pantalla sigue mostrando los valores viejos sin actualizarse, lo cual demuestra que el problema principal radica en que quien escribe el NavHost es quien tiene que acordarse de llamarlas, creando una dependencia frágil en el lugar más lejano al dato que el uso de Flow resuelve al eliminar la necesidad de consultar manualmente.

Ejercicio D1:
Al editar o vender un producto el SELECT de la lista se ejecuta una sola vez y no lo pide nadie, sino que es disparado automáticamente por el InvalidationTracker de Room, el cual detecta que la tabla productos cambió y reejecuta la consulta para notificar a la pantalla sin necesidad de llamadas manuales a un método recargar().

Observación del D3:
Al modificar ProductoEntity agregando un campo sin actualizar la versión (version = 1) en InventarioDatabase, Room detecta que la firma del esquema cambió y lanza un IllegalStateException, impidiendo que la app corra para evitar corromper los datos existentes en SQLite.
