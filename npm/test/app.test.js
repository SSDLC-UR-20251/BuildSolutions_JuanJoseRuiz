const { transferir, calcularSaldo, leerArchivo, escribirArchivo } = require('../src/app');

// Test: Transferencia exitosa
test('Transferencia entre cuentas', () => {


    // Intentar realizar una transferencia
    const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 30);
    
    // Comprobar que la transferencia fue exitosa
    expect(resultado.exito).toBe(true);
    expect(resultado.mensaje).toBe('Transferencia de 30 realizada correctamente de juan.jose@urosario.edu.co a sara.palaciosc@urosario.edu.co.');

});

// Test: Transferencia con saldo insuficiente
test('Transferencia con saldo insuficiente', () => {

    // Intentar realizar una transferencia con un monto mayor al saldo
    const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 1000);
    
    // Comprobar que la transferencia falló debido a saldo insuficiente
    expect(resultado.exito).toBe(false);
    expect(resultado.mensaje).toBe('Saldo insuficiente en la cuenta de juan.jose@urosario.edu.co.');
});
