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

public class PokemonTest {
    private Pokemon pokemon;
    private Tipo tipoFuego;
    private Tipo tipoAgua;
    private List<Movimiento> movimientos;
    
    @Before
    public void setUp() {
        tipoFuego = new Tipo("Fire");
        tipoAgua = new Tipo("Water");
        movimientos = new ArrayList<>();
        
        // Crear un movimiento de prueba
        MovimientoFisico tacleada = new MovimientoFisico(
            "Tacleada", 
            new Tipo("Normal"), 
            40,    // poder 
            100,   // precisión
            35,    // PP actual
            35,    // PP máximo
            false, // no tiene prioridad
            "Físico",
            "Un ataque físico básico"
        );
        movimientos.add(tacleada);
        
        // Crear pokémon de prueba usando el constructor correcto
        pokemon = new Pokemon(
            100,   // PS actual
            100,   // PS máximo (maxPs)
            80,    // Ataque
            70,    // Defensa
            60,    // Velocidad
            75,    // Ataque Especial
            65,    // Defensa Especial
            "Charmander",
            tipoFuego,
            null,   // Sin tipo secundario
            movimientos
        );
    }
    
    @Test
    public void testRecibirDano() {
        pokemon.recibirDaño(30);
        assertEquals(70, pokemon.getPs());
        
        pokemon.recibirDaño(80);
        assertEquals(0, pokemon.getPs());
        assertTrue(pokemon.estaDebilitado());
    }
    
    @Test
    public void testCurar() {
        pokemon.recibirDaño(50);
        assertEquals(50, pokemon.getPs());
        
        pokemon.curar(20);
        assertEquals(70, pokemon.getPs());
        
        // Verificar que no se cura más allá del máximo
        pokemon.curar(50);
        assertEquals(100, pokemon.getPs());
    }
    
    @Test
    public void testRevivir() {
        pokemon.recibirDaño(100);
        assertTrue(pokemon.estaDebilitado());
        
        pokemon.revivir(50);
        assertEquals(50, pokemon.getPs());
        assertFalse(pokemon.estaDebilitado());
        
        // Verificar que revivir no hace nada si ya está vivo
        pokemon.revivir(30);
        assertEquals(50, pokemon.getPs());
    }
    
    @Test
    public void testEstadosPokemon() {
        assertNull(pokemon.getStatus());
        assertFalse(pokemon.tieneEstado());
        
        // Usar establecerEstado en lugar de setEstado para activar las estrategias
        pokemon.establecerEstado("paralizado");
        assertEquals("paralizado", pokemon.getStatus());
        assertTrue(pokemon.tieneEstado());
        
        // Probar curar estados
        pokemon.curarEstados();
        assertNull(pokemon.getStatus());
        assertFalse(pokemon.tieneEstado());
    }
    
    @Test
    public void testConstructorCopia() {
        Pokemon copia = new Pokemon(pokemon);
        
        // Verificar que los atributos se copian correctamente
        assertEquals(pokemon.getName(), copia.getName());
        assertEquals(pokemon.getPs(), copia.getPs());
        assertEquals(pokemon.getMaxPs(), copia.getMaxPs());
        assertEquals(pokemon.getAttack(), copia.getAttack());
        assertEquals(pokemon.getDefense(), copia.getDefense());
        assertEquals(pokemon.getSpeed(), copia.getSpeed());
        assertEquals(pokemon.getSpecialAttack(), copia.getSpecialAttack());
        assertEquals(pokemon.getSpecialDefense(), copia.getSpecialDefense());
        assertEquals(pokemon.getTipo1().getNombre(), copia.getTipo1().getNombre());
        
        // Verificar que se trata de una copia profunda
        pokemon.setPs(50);
        assertEquals(100, copia.getPs());
        
        // Verificar que los movimientos también se copian
        assertEquals(pokemon.getMovimientos().size(), copia.getMovimientos().size());
        assertEquals(pokemon.getMovimientos().get(0).getNombre(), copia.getMovimientos().get(0).getNombre());
        
        // Verificar que las listas de movimientos son independientes
        assertNotSame(pokemon.getMovimientos(), copia.getMovimientos());
    }
    
    @Test
    public void testPuedeAtacar() {
        // Sin estado, debería poder atacar
        assertTrue(pokemon.puedeAtacar());
        
        // Con estado dormido, no debería poder atacar (usando establecerEstado)
        pokemon.establecerEstado("dormido");
        assertFalse(pokemon.puedeAtacar());
        
        // Con estado congelado, no debería poder atacar
        pokemon.establecerEstado("congelado");
        // Para tipo fuego, debería poder atacar (inmune a congelación)
        assertTrue(pokemon.puedeAtacar());
        
        // Crear un pokemon no de fuego para probar congelación
        Pokemon pokemonAgua = new Pokemon(100, 100, 80, 70, 60, 75, 65, "Squirtle", tipoAgua, null, movimientos);
        pokemonAgua.establecerEstado("congelado");
        assertFalse(pokemonAgua.puedeAtacar());
    }
    
    @Test
    public void testPrecision() {
        assertEquals(1.0, pokemon.getMultiplicadorPrecision(), 0.001);
        
        pokemon.reducirPrecision(0.9);
        assertEquals(0.9, pokemon.getMultiplicadorPrecision(), 0.001);
        
        pokemon.aumentarPrecision(1.2);
        assertEquals(1.08, pokemon.getMultiplicadorPrecision(), 0.001);
    }
    
    @Test
    public void testAplicarEfectosEstado() {
        // Sin estado, no debería retornar ningún mensaje
        String resultado = pokemon.aplicarEfectosEstado();
        assertEquals("", resultado);
        
        // Con estado quemado, debería aplicar daño
        pokemon.establecerEstado("quemado");
        int psAntes = pokemon.getPs();
        resultado = pokemon.aplicarEfectosEstado();
        
        // Verificar que recibió daño
        assertTrue(pokemon.getPs() < psAntes);
        assertTrue(resultado.contains("sufre"));
        assertTrue(resultado.contains("daño por quemadura"));
    }
    
    @Test
    public void testProcesarIntentarAtacar() {
        // Sin estado, debería retornar null (puede atacar)
        String resultado = pokemon.procesarIntentarAtacar();
        assertNull(resultado);
        
        // Con estado dormido, debería retornar mensaje
        pokemon.establecerEstado("dormido");
        resultado = pokemon.procesarIntentarAtacar();
        assertNotNull(resultado);
        assertTrue(resultado.contains("dormido"));
        
        // Con estado confundido, puede retornar mensaje o null (probabilístico)
        pokemon.establecerEstado("confundido");
        // No podemos testear esto de forma determinística debido a la aleatoriedad
        // pero podemos verificar que no lance excepción
        assertNotNull(pokemon.procesarIntentarAtacar()); // Puede ser null o un mensaje
    }
}