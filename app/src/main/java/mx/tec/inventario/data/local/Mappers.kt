package mx.tec.inventario.data.local

import mx.tec.inventario.domain.Producto

/**
 * Los traductores entre la tabla y el dominio.
 *
 * Son aburridos a propósito: el día que la columna se llame distinto, o que la
 * base guarde el precio en centavos, el temblor se detiene en este archivo.
 */
fun ProductoEntity.toDomain() = Producto(
    id = id,
    nombre = nombre,
    precio = precio,
    cantidad = cantidad
)

fun Producto.toEntity() = ProductoEntity(
    id = id,
    nombre = nombre,
    precio = precio,
    cantidad = cantidad
)