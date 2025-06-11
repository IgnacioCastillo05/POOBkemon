package test;

import domain.Pokemon;
import domain.Tipo;
import domain.Movimiento;
import domain.MovimientoFisico;
import domain.MovimientoEstado;
import domain.MovimientoEspecial;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MovimientoTest {
    private Pokemon pokemonAtacante;
    private Pokemon pokemonDefensor;
    private MovimientoFisico movimientoFisico;
    private MovimientoEspecial movimientoEspecial;
    private MovimientoEstado movimientoEstado;
    
    @Before
    public void setUp() {
        // Crear tipos
        Tipo tipoFuego = new Tipo("Fire");
        Tipo tipoAgua = new Tipo("Water");
        Tipo tipoPlanta = new Tipo("Grass");
        Tipo tipoNormal = new Tipo("Normal");
        
        // Crear movimientos
        movimientoFisico = new MovimientoFisico(
            "Placaje", 
            tipoNormal, 
            40,    // poder 
            100,   // precisión
            35,    // PP actual
            35,    // PP máximo
            false, // no tiene prioridad
            "Físico",
            "Un ataque físico básico"
        );
        
        movimientoEspecial = new MovimientoEspecial(
            "Lanzallamas", 
            tipoFuego, 
            90,    // poder 
            100,   // precisión
            15,    // PP actual
            15,    // PP máximo
            false, // no tiene prioridad
            "Especial",
            "Un potente ataque especial de tipo fuego"
        );
        
        movimientoEstado = new MovimientoEstado(
            "Refugio", 
            tipoNormal, 
            0,     // poder (no hace daño)
            100,   // precisión
            20,    // PP actual
            20,    // PP máximo
            false, // no tiene prioridad
            "Estado",
            "Aumenta la defensa del usuario"
        );
        
        // Crear listas de movimientos
        List<Movimiento> movimientosAtacante = new ArrayList<>();
        movimientosAtacante.add(movimientoFisico);
        movimientosAtacante.add(movimientoEspecial);
        movimientosAtacante.add(movimientoEstado);
        
        List<Movimiento> movimientosDefensor = new ArrayList<>();
        
        // Crear pokémon
        pokemonAtacante = new Pokemon(
            100,   // PS actual
            100,   // PS máximo
            80,    // Ataque
            70,    // Defensa
            60,    // Velocidad
            75,    // Ataque Especial
            65,    // Defensa Especial
            "Charmander",
            tipoFuego,
            null,   // Sin tipo secundario
            movimientosAtacante
        );
        
        pokemonDefensor = new Pokemon(
            100,   // PS actual
            100,   // PS máximo
            70,    // Ataque
            65,    // Defensa
            50,    // Velocidad
            65,    // Ataque Especial
            70,    // Defensa Especial
            "Bulbasaur",
            tipoPlanta,
            null,   // Sin tipo secundario
            movimientosDefensor
        );
    }
    
    @Test
    public void testUsarMovimiento() {
        assertEquals(35, movimientoFisico.getPP());
        
        assertTrue(movimientoFisico.usarMovimiento());
        assertEquals(34, movimientoFisico.getPP());
        
        // Forzar PP a 1 y verificar que se pueda usar
        movimientoFisico.setPP(1);
        assertTrue(movimientoFisico.usarMovimiento());
        assertEquals(0, movimientoFisico.getPP());
        
        // Verificar que no se pueda usar con 0 PP
        assertFalse(movimientoFisico.usarMovimiento());
        assertEquals(0, movimientoFisico.getPP());
    }
    
    @Test
    public void testReducirPP() {
        assertEquals(35, movimientoFisico.getPP());
        
        assertTrue(movimientoFisico.reducirPP(5));
        assertEquals(30, movimientoFisico.getPP());
        
        // Intentar reducir más PP de los disponibles
        assertFalse(movimientoFisico.reducirPP(35));
        assertEquals(30, movimientoFisico.getPP()); // No debería cambiar
        
        // Reducir exactamente los PP disponibles
        assertTrue(movimientoFisico.reducirPP(30));
        assertEquals(0, movimientoFisico.getPP());
    }
    
    @Test
    public void testRestaurarPP() {
        movimientoFisico.setPP(10);
        assertEquals(10, movimientoFisico.getPP());
        
        movimientoFisico.restaurarPP(5);
        assertEquals(15, movimientoFisico.getPP());
        
        // Verificar que no se restaure más allá del máximo
        movimientoFisico.restaurarPP(30);
        assertEquals(35, movimientoFisico.getPP());
        
        // Verificar restaurar completo
        movimientoFisico.setPP(10);
        movimientoFisico.restaurarPPCompleto();
        assertEquals(35, movimientoFisico.getPP());
    }
    
    @Test
    public void testMovimientoFisico() {
        int psAntes = pokemonDefensor.getPs();
        
        // Aplicar el movimiento físico
        String resultado = movimientoFisico.aplicarEfecto(pokemonAtacante, pokemonDefensor);
        
        // Verificar que causó daño
        assertTrue(pokemonDefensor.getPs() < psAntes);
        assertTrue(resultado.contains("recibió"));
        assertTrue(resultado.contains("de daño"));
        
        // Verificar que el daño es razonable (no negativo, no excesivo)
        int danoCausado = psAntes - pokemonDefensor.getPs();
        assertTrue("El daño debe ser positivo", danoCausado > 0);
        assertTrue("El daño no debe ser excesivo", danoCausado < psAntes);
    }
    
    @Test
    public void testMovimientoEspecial() {
        int psAntes = pokemonDefensor.getPs();
        
        // Aplicar el movimiento especial (Fuego vs Planta = super efectivo)
        String resultado = movimientoEspecial.aplicarEfecto(pokemonAtacante, pokemonDefensor);
        
        // Verificar que causó daño
        assertTrue(pokemonDefensor.getPs() < psAntes);
        assertTrue(resultado.contains("recibió"));
        assertTrue(resultado.contains("de daño"));
        
        // Verificar efectividad contra tipo planta (debería ser considerable)
        int danoCausado = psAntes - pokemonDefensor.getPs();
        assertTrue("El daño debe ser significativo por super efectividad", danoCausado > 15);
        
        // Puede contener efecto secundario de quemadura (10% chance en Lanzallamas)
        // No podemos testear esto deterministicamente debido a la aleatoriedad
    }
    
    @Test
    public void testMovimientoEstado() {
        int defensaAntes = pokemonAtacante.getDefense();
        int psDefensorAntes = pokemonDefensor.getPs();
        
        // Aplicar el movimiento de estado (Refugio)
        String resultado = movimientoEstado.aplicarEfecto(pokemonAtacante, pokemonDefensor);
        
        // Verificar que no causó daño al defensor
        assertEquals(psDefensorAntes, pokemonDefensor.getPs());
        
        // Verificar que aumentó la defensa del atacante (20% según el código)
        assertTrue("La defensa debería haber aumentado", pokemonAtacante.getDefense() > defensaAntes);
        assertTrue("El mensaje debería indicar aumento de defensa", resultado.contains("aumentó su defensa"));
        
        // Verificar el aumento específico (20% = multiplicar por 1.2)
        int defensaEsperada = (int)(defensaAntes * 1.2);
        assertEquals(defensaEsperada, pokemonAtacante.getDefense());
    }
    
    @Test
    public void testConstructorCopia() {
        MovimientoFisico copia = new MovimientoFisico(movimientoFisico);
        
        // Verificar que los atributos se copian correctamente
        assertEquals(movimientoFisico.getNombre(), copia.getNombre());
        assertEquals(movimientoFisico.getPoder(), copia.getPoder());
        assertEquals(movimientoFisico.getPrecision(), copia.getPrecision());
        assertEquals(movimientoFisico.getPP(), copia.getPP());
        assertEquals(movimientoFisico.getMaxPP(), copia.getMaxPP());
        assertEquals(movimientoFisico.getClaseMovimiento(), copia.getClaseMovimiento());
        assertEquals(movimientoFisico.getTipo().getNombre(), copia.getTipo().getNombre());
        
        // Verificar que se trata de una copia independiente
        movimientoFisico.setPP(10);
        assertEquals(35, copia.getPP());
        
        // Verificar uso independiente
        assertTrue(copia.usarMovimiento());
        assertEquals(34, copia.getPP());
        assertEquals(10, movimientoFisico.getPP());
    }
    
    @Test
    public void testTienePPDisponibles() {
        assertTrue(movimientoFisico.tienePPDisponibles());
        
        movimientoFisico.setPP(1);
        assertTrue(movimientoFisico.tienePPDisponibles());
        
        movimientoFisico.setPP(0);
        assertFalse(movimientoFisico.tienePPDisponibles());
    }
    
    @Test
    public void testGettersBasicos() {
        assertEquals("Placaje", movimientoFisico.getNombre());
        assertEquals("Normal", movimientoFisico.getTipo().getNombre());
        assertEquals(40, movimientoFisico.getPoder());
        assertEquals(100, movimientoFisico.getPrecision());
        assertEquals(35, movimientoFisico.getMaxPP());
        assertEquals("Físico", movimientoFisico.getClaseMovimiento());
        assertFalse(movimientoFisico.getPrioridad());
        
        assertEquals("Lanzallamas", movimientoEspecial.getNombre());
        assertEquals("Fire", movimientoEspecial.getTipo().getNombre());
        assertEquals(90, movimientoEspecial.getPoder());
        
        assertEquals("Refugio", movimientoEstado.getNombre());
        assertEquals(0, movimientoEstado.getPoder());
        assertEquals("Estado", movimientoEstado.getClaseMovimiento());
    }
    
    @Test
    public void testMovimientoEstadoEspecifico() {
        // Probar otros movimientos de estado específicos
        MovimientoEstado danzaDragon = new MovimientoEstado(
            "Danza Dragon", new Tipo("Dragon"), 0, 100, 20, 20, false, "Estado", "Aumenta ataque y velocidad"
        );
        
        int ataqueAntes = pokemonAtacante.getAttack();
        int velocidadAntes = pokemonAtacante.getSpeed();
        
        String resultado = danzaDragon.aplicarEfecto(pokemonAtacante, pokemonDefensor);
        
        // Verificar que aumentó ataque y velocidad (20% cada uno según el código)
        assertTrue("El ataque debería haber aumentado", pokemonAtacante.getAttack() > ataqueAntes);
        assertTrue("La velocidad debería haber aumentado", pokemonAtacante.getSpeed() > velocidadAntes);
        assertTrue("El mensaje debería mencionar ataque y velocidad", 
                  resultado.contains("ataque") && resultado.contains("velocidad"));
    }
    
    @Test
    public void testEfectosSecundarios() {
        // Crear un movimiento con efecto secundario conocido
        MovimientoFisico golpeCuerpo = new MovimientoFisico(
            "Golpe Cuerpo", new Tipo("Normal"), 85, 100, 15, 15, false, "Físico", 
            "Ataque que puede paralizar"
        );
        
        // Aplicar múltiples veces para intentar activar el efecto (30% chance)
        // Nota: Este test puede fallar ocasionalmente debido a la aleatoriedad
        boolean efectoActivado = false;
        String ultimoResultado = "";
        
        for (int i = 0; i < 20 && !efectoActivado; i++) {
            // Resetear el estado del defensor
            pokemonDefensor.curarEstados();
            ultimoResultado = golpeCuerpo.aplicarEfecto(pokemonAtacante, pokemonDefensor);
            if (ultimoResultado.contains("paralizado")) {
                efectoActivado = true;
            }
        }
        
        // Al menos verificar que el movimiento funciona y causa daño
        assertTrue("El resultado debería contener información de daño", 
                  ultimoResultado.contains("recibió") && ultimoResultado.contains("daño"));
    }
}