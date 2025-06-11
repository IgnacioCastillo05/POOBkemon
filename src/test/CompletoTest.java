package test;

import domain.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests de integración para verificar el funcionamiento conjunto
 * de los componentes del sistema POOBkemon
 */
public class CompletoTest {
    
    private EntrenadorPersona entrenador1;
    private EntrenadorPersona entrenador2;
    private Pokemon charmander;
    private Pokemon squirtle;
    private Pokemon bulbasaur;
    private List<Movimiento> movimientosCharmander;
    private List<Movimiento> movimientosSquirtle;
    
    @Before
    public void setUp() throws Exception {
        // Crear tipos
        Tipo tipoFuego = new Tipo("Fire");
        Tipo tipoAgua = new Tipo("Water");
        Tipo tipoPlanta = new Tipo("Grass");
        Tipo tipoNormal = new Tipo("Normal");
        
        // Crear movimientos para Charmander
        movimientosCharmander = new ArrayList<>();
        movimientosCharmander.add(new MovimientoFisico(
            "Arañazo", tipoNormal, 40, 100, 35, 35, false, "Físico", "Ataque con garras"
        ));
        movimientosCharmander.add(new MovimientoEspecial(
            "Lanzallamas", tipoFuego, 90, 100, 15, 15, false, "Especial", "Ataque de fuego"
        ));
        movimientosCharmander.add(new MovimientoEstado(
            "Refugio", tipoNormal, 0, 100, 20, 20, false, "Estado", "Aumenta defensa"
        ));
        
        // Crear movimientos para Squirtle
        movimientosSquirtle = new ArrayList<>();
        movimientosSquirtle.add(new MovimientoFisico(
            "Placaje", tipoNormal, 40, 100, 35, 35, false, "Físico", "Embestida"
        ));
        movimientosSquirtle.add(new MovimientoEspecial(
            "Pistola Agua", tipoAgua, 40, 100, 25, 25, false, "Especial", "Chorro de agua"
        ));
        
        // Crear Pokémon
        charmander = new Pokemon(
            100, 100, 80, 70, 65, 85, 65, "Charmander", tipoFuego, null, movimientosCharmander
        );
        
        squirtle = new Pokemon(
            100, 100, 70, 80, 55, 70, 75, "Squirtle", tipoAgua, null, movimientosSquirtle
        );
        
        bulbasaur = new Pokemon(
            100, 100, 75, 75, 60, 80, 80, "Bulbasaur", tipoPlanta, null, new ArrayList<>()
        );
        
        // Crear entrenadores
        entrenador1 = new EntrenadorPersona("Ash");
        entrenador2 = new EntrenadorPersona("Gary");
        
        // Agregar Pokémon a los entrenadores
        entrenador1.agregarPokemon(charmander);
        entrenador1.agregarPokemon(bulbasaur);
        
        entrenador2.agregarPokemon(squirtle);
        
        // Agregar items
        entrenador1.agregarItem(new NormalPotion());
        entrenador1.agregarItem(new SuperPotion());
        entrenador1.agregarItem(new Revive());
        
        entrenador2.agregarItem(new HyperPotion());
        entrenador2.agregarItem(new NormalPotion());
    }
    
    @Test
    public void testEntrenadorConPokemonYItems() {
        // Verificar que los entrenadores tienen sus Pokémon
        assertEquals(2, entrenador1.getPokemones().size());
        assertEquals(1, entrenador2.getPokemones().size());
        
        // Verificar que tienen items
        assertEquals(3, entrenador1.getItems().size());
        assertEquals(2, entrenador2.getItems().size());
        
        // Verificar que pueden combatir
        assertTrue(entrenador1.puedeCombatir());
        assertTrue(entrenador2.puedeCombatir());
    }
    
    @Test
    public void testUsarItemEnPokemon() throws POOBkemonException {
        // Dañar un Pokémon
        charmander.recibirDaño(50);
        assertEquals(50, charmander.getPs());
        
        // Usar una poción
        NormalPotion potion = new NormalPotion();
        entrenador1.usarItem(potion, charmander);
        
        // Verificar que se curó
        assertEquals(70, charmander.getPs());
        
        // Verificar que el item se eliminó de la bolsa 
        assertEquals(2, entrenador1.getItems().size());
    }
    
    @Test
    public void testBatallaSimulada() {
        // Simular un ataque entre Pokémon
        int psSquirtleAntes = squirtle.getPs();
        
        // Charmander usa Lanzallamas contra Squirtle (Fuego vs Agua = no muy efectivo)
        MovimientoEspecial lanzallamas = (MovimientoEspecial) charmander.getMovimientos().get(1);
        String resultado = lanzallamas.aplicarEfecto(charmander, squirtle);
        
        // Verificar que causó daño
        assertTrue(squirtle.getPs() < psSquirtleAntes);
        assertTrue(resultado.contains("recibió"));
        
        // El daño debería ser reducido por la resistencia de tipo
        int dañoCausado = psSquirtleAntes - squirtle.getPs();
        assertTrue("El daño debería ser moderado por resistencia de tipo", dañoCausado < 50);
    }
    
    @Test
    public void testEfectividadesDeTipo() {
        int psInicialBulbasaur = bulbasaur.getPs();
        
        // Charmander (Fuego) ataca a Bulbasaur (Planta) = super efectivo
        MovimientoEspecial lanzallamas = (MovimientoEspecial) charmander.getMovimientos().get(1);
        lanzallamas.aplicarEfecto(charmander, bulbasaur);
        
        int dañoContraBulbasaur = psInicialBulbasaur - bulbasaur.getPs();
        
        // Resetear squirtle para comparar
        squirtle.setPs(100);
        int psInicialSquirtle = squirtle.getPs();
        
        // Charmander (Fuego) ataca a Squirtle (Agua) = no muy efectivo
        lanzallamas.aplicarEfecto(charmander, squirtle);
        int dañoContraSquirtle = psInicialSquirtle - squirtle.getPs();
        
        // El daño contra Bulbasaur debería ser mayor que contra Squirtle
        assertTrue("El daño super efectivo debería ser mayor que el no muy efectivo", 
                  dañoContraBulbasaur > dañoContraSquirtle);
    }
    
    @Test
    public void testEstadosYEfectos() {
        // Aplicar estado quemado
        charmander.establecerEstado("quemado");
        assertTrue(charmander.tieneEstado());
        assertEquals("quemado", charmander.getStatus());
        
        // Aplicar efectos de estado
        int psAntes = charmander.getPs();
        String mensaje = charmander.aplicarEfectosEstado();
        
        // Verificar que recibió daño por quemadura
        assertTrue(charmander.getPs() < psAntes);
        assertTrue(mensaje.contains("quemadura"));
        
        // Curar estado
        charmander.curarEstados();
        assertFalse(charmander.tieneEstado());
    }
    
    @Test
    public void testReviveYDebilitacion() throws POOBkemonException {
        // Debilitar un Pokémon
        charmander.recibirDaño(100);
        assertTrue(charmander.estaDebilitado());
        assertEquals(0, charmander.getPs());
        
        // Verificar que el entrenador ya no puede usar este Pokémon en combate
        // (pero aún puede combatir con el otro)
        assertTrue(entrenador1.puedeCombatir());
        
        // Usar Revive
        Revive revive = new Revive();
        entrenador1.usarItem(revive, charmander);
        
        // Verificar que revivió con la mitad de PS
        assertFalse(charmander.estaDebilitado());
        assertEquals(50, charmander.getPs());
        
        // Verificar que el item se eliminó
        assertEquals(2, entrenador1.getItems().size());
    }
    
    @Test
    public void testConstructoresCopia() {
        // Test constructor copia Pokemon
        Pokemon copiaCharmander = new Pokemon(charmander);
        assertEquals(charmander.getName(), copiaCharmander.getName());
        assertEquals(charmander.getPs(), copiaCharmander.getPs());
        assertNotSame(charmander, copiaCharmander);
        
        // Modificar original no afecta copia
        charmander.recibirDaño(25);
        assertNotEquals(charmander.getPs(), copiaCharmander.getPs());
        
        // Test constructor copia Movimiento
        MovimientoFisico arañazo = (MovimientoFisico) charmander.getMovimientos().get(0);
        MovimientoFisico copiaArañazo = new MovimientoFisico(arañazo);
        assertEquals(arañazo.getNombre(), copiaArañazo.getNombre());
        assertEquals(arañazo.getPP(), copiaArañazo.getPP());
        
        // Usar original no afecta copia
        arañazo.usarMovimiento();
        assertNotEquals(arañazo.getPP(), copiaArañazo.getPP());
    }
    
    @Test
    public void testPPYMovimientos() {
        MovimientoFisico arañazo = (MovimientoFisico) charmander.getMovimientos().get(0);
        int ppIniciales = arañazo.getPP();
        
        // Usar movimiento múltiples veces
        for (int i = 0; i < 5; i++) {
            assertTrue(arañazo.usarMovimiento());
        }
        
        assertEquals(ppIniciales - 5, arañazo.getPP());
        
        // Restaurar PP
        arañazo.restaurarPP(3);
        assertEquals(ppIniciales - 2, arañazo.getPP());
        
        // Restaurar completamente
        arañazo.restaurarPPCompleto();
        assertEquals(ppIniciales, arañazo.getPP());
    }
    
    @Test
    public void testEntrenadorSinPokemonDisponibles() {
        // Debilitar todos los Pokémon del entrenador 2
        squirtle.recibirDaño(100);
        assertTrue(squirtle.estaDebilitado());
        
        // El entrenador ya no puede combatir
        assertFalse(entrenador2.puedeCombatir());
        
        // No debería tener primer Pokémon disponible
        assertNull(entrenador2.getPrimerPokemonDisponible());
    }
    
    @Test(expected = POOBkemonException.class)
    public void testUsarItemNoDisponible() throws POOBkemonException {
        // Intentar usar un item que no está en la bolsa
        HyperPotion hyperPotion = new HyperPotion();
        entrenador1.usarItem(hyperPotion, charmander);
    }
    
    @Test
    public void testEstadoEquipo() {
        String estadoEquipo = entrenador1.getEstadoEquipo();
        
        // Verificar que contiene información del equipo
        assertTrue(estadoEquipo.contains("Ash"));
        assertTrue(estadoEquipo.contains("Charmander"));
        assertTrue(estadoEquipo.contains("Bulbasaur"));
        assertTrue(estadoEquipo.contains("PS"));
        
        // Debilitar un Pokémon y verificar que aparece en el estado
        charmander.recibirDaño(100);
        estadoEquipo = entrenador1.getEstadoEquipo();
        assertTrue(estadoEquipo.contains("DEBILITADO"));
    }
}