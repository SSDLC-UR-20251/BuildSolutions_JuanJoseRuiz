const fs = require('fs');

// Función para leer el archivo transactions.txt
function leerArchivo() {
    try {
        const data = fs.readFileSync('/workspaces/BuildSolutions_JuanJoseRuiz/npm/src/resources/transactions.txt', 'utf8');
        if (data) {
            return JSON.parse(data); // Parseamos el JSON para obtener un objeto con las transacciones de cada usuario
        }
        return {}; // Si el archivo está vacío, devolvemos un objeto vacío
    } catch (error) {
        console.error('Error al leer el archivo:', error);
        return {}; // En caso de error, devolvemos un objeto vacío
    }
}

// Función para escribir el archivo transactions.txt
function escribirArchivo(data) {
    try {
        fs.writeFileSync('/workspaces/BuildSolutions_JuanJoseRuiz/npm/src/resources/transactions.txt', JSON.stringify(data, null, 2)); // Guarda el archivo en formato JSON
        console.log('Archivo guardado correctamente.');
    } catch (error) {
        console.error('Error al guardar el archivo:', error);
    }
}

// Función para calcular el saldo actual de un usuario, basado en sus transacciones
function calcularSaldo(usuario) {
    const transaccionesPorUsuario = leerArchivo(); // Esto ahora es un objeto, no un arreglo
    const transacciones = transaccionesPorUsuario[usuario] || []; // Accedemos a las transacciones del usuario o devolvemos un arreglo vacío si no existen

    let saldo = 0;

    // Iteramos sobre las transacciones del usuario y calculamos el saldo
    transacciones.forEach(transaccion => {
        const monto = parseFloat(transaccion.balance); // Convertimos el balance a un número flotante
        if (transaccion.type === 'Deposit') {
            saldo += monto; // Sumamos el depósito
        } else if (transaccion.type === 'Withdrawal') {
            saldo += monto; // Restamos el retiro
        }
    });

    return saldo; // Devolvemos el saldo total
}


// Función para realizar la transferencia entre cuentas
function transferir(de, para, monto) {
    const transaccionesPorUsuario = leerArchivo(); // Leer el archivo de transacciones
    const saldoDe = calcularSaldo(de); // Calcular el saldo de la cuenta de origen

    // Verificar si hay saldo suficiente en la cuenta de origen
    if (saldoDe < monto) {
        return {
            exito: false,
            mensaje: `Saldo insuficiente en la cuenta de ${de}.`
        };
    }

    // Verificar si las cuentas de origen y destino existen en las transacciones
    if (!transaccionesPorUsuario[de]) {
        transaccionesPorUsuario[de] = [];
    }
    if (!transaccionesPorUsuario[para]) {
        transaccionesPorUsuario[para] = [];
    }

    // Registrar solo la nueva transacción en el archivo, sin alterar el saldo actual de las cuentas
    const nuevaTransaccionDe = {
        balance: `-${monto}`,  // Retiro de la cuenta de origen
        type: 'Withdrawal',
        timestamp: new Date().toISOString()  // Marca la fecha y hora de la transferencia
    };
    
    const nuevaTransaccionPara = {
        balance: `${monto}`,  // Depósito en la cuenta de destino
        type: 'Deposit',
        timestamp: new Date().toISOString()  // Marca la fecha y hora de la transferencia
    };

    // Agregar las nuevas transacciones
    transaccionesPorUsuario[de].push(nuevaTransaccionDe);
    transaccionesPorUsuario[para].push(nuevaTransaccionPara);

    // Guardar solo las nuevas transacciones en el archivo, sin modificar otras transacciones
    escribirArchivo(transaccionesPorUsuario);

    return {
        exito: true,
        mensaje: `Transferencia de ${monto} realizada correctamente de ${de} a ${para}.`
    };
}




// Ejemplo de uso
const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 50);
console.log(resultado.mensaje);

// Exportar las funciones para pruebas
module.exports = { transferir, calcularSaldo, leerArchivo, escribirArchivo };
