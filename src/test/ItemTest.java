package test;

import domain.Pokemon;
import domain.Revive;
import domain.SuperPotion;
import domain.Tipo;
import domain.HyperPotion;
import domain.NormalPotion;
import domain.POOBkemonException;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class ItemTest {
    
    private Pokemon pokemon;
    private Pokemon pokemonDebilitado;
    private NormalPotion normalPotion;
    private SuperPotion superPotion;
    private HyperPotion hyperPotion;
    private Revive revive;
    
    @Before
    public void setUp() {
        // Crear un Pokémon para las pruebas
        Tipo tipoFuego = new Tipo("Fire");
        pokemon = new Pokemon(
            100, 100, 80, 70, 60, 75, 65, "Charmander", tipoFuego, null, new ArrayList<>()
        );
        
        // Crear un Pokémon debilitado
        pokemonDebilitado = new Pokemon(
            0, 100, 80, 70, 60, 75, 65, "Fainted", tipoFuego, null, new ArrayList<>()
        );
        
        // Crear los items
        normalPotion = new NormalPotion();
        superPotion = new SuperPotion();
        hyperPotion = new HyperPotion();
        revive = new Revive();
    }
    
    @Test
    public void testNormalPotion() throws POOBkemonException {
        // Dañar al Pokémon
        pokemon.recibirDaño(30);
        assertEquals(70, pokemon.getPs());
        
        // Usar poción normal (cura 20 PS)
        normalPotion.usar(pokemon);
        assertEquals(90, pokemon.getPs());
        
        // Verificar que no cura más allá del máximo
        normalPotion.usar(pokemon);
        assertEquals(100, pokemon.getPs());
    }
    
    @Test
    public void testSuperPotion() throws POOBkemonException {
        // Dañar al Pokémon
        pokemon.recibirDaño(60);
        assertEquals(40, pokemon.getPs());
        
        // Usar super poción (cura 50 PS)
        superPotion.usar(pokemon);
        assertEquals(90, pokemon.getPs());
        
        // Verificar que no cura más allá del máximo
        superPotion.usar(pokemon);
        assertEquals(100, pokemon.getPs());
    }
    
    @Test
    public void testHyperPotion() throws POOBkemonException {
        // Dañar al Pokémon
        pokemon.recibirDaño(80);
        assertEquals(20, pokemon.getPs());
        
        // Usar hiper poción (cura 100 PS)
        hyperPotion.usar(pokemon);
        assertEquals(100, pokemon.getPs());
        
        // Verificar que no cura más allá del máximo cuando ya está full
        hyperPotion.usar(pokemon);
        assertEquals(100, pokemon.getPs());
    }
    
    @Test
    public void testRevive() throws POOBkemonException {
        // Verificar que el Pokémon está debilitado
        assertTrue(pokemonDebilitado.estaDebilitado());
        assertEquals(0, pokemonDebilitado.getPs());
        
        // Usar revivir (devuelve la mitad de PS máximos)
        revive.usar(pokemonDebilitado);
        assertEquals(0, pokemonDebilitado.getPs());
        assertFalse(pokemonDebilitado.estaDebilitado());
    }
    
    @Test(expected = POOBkemonException.class)
    public void testUsarPotionEnPokemonDebilitado() throws POOBkemonException {
        // Intentar usar poción en un Pokémon debilitado debe lanzar excepción
        normalPotion.usar(pokemonDebilitado);
    }
    
    @Test(expected = POOBkemonException.class)
    public void testUsarSuperPotionEnPokemonDebilitado() throws POOBkemonException {
        superPotion.usar(pokemonDebilitado);
    }
    
    @Test(expected = POOBkemonException.class)
    public void testUsarHyperPotionEnPokemonDebilitado() throws POOBkemonException {
        hyperPotion.usar(pokemonDebilitado);
    }
    
    @Test(expected = POOBkemonException.class)
    public void testUsarReviveEnPokemonNoDebilitado() throws POOBkemonException {
        // Pokémon con vida
        assertEquals(100, pokemon.getPs());
        assertFalse(pokemon.estaDebilitado());
        
        // Intentar usar revivir en un Pokémon no debilitado debe lanzar excepción
        revive.usar(pokemon);
    }
    
    @Test(expected = POOBkemonException.class)
    public void testUsarNormalPotionEnPokemonNulo() throws POOBkemonException {
        normalPotion.usar(null);
    }
    
    @Test(expected = POOBkemonException.class)
    public void testUsarSuperPotionEnPokemonNulo() throws POOBkemonException {
        superPotion.usar(null);
    }
    
    @Test(expected = POOBkemonException.class)
    public void testUsarHyperPotionEnPokemonNulo() throws POOBkemonException {
        hyperPotion.usar(null);
    }
    
    @Test(expected = POOBkemonException.class)  
    public void testUsarReviveEnPokemonNulo() throws POOBkemonException {
        revive.usar(null);
    }
    
    @Test
    public void testPotionWithFullHP() throws POOBkemonException {
        // Pokémon con vida completa
        assertEquals(100, pokemon.getPs());
        
        // Usar poción no debería cambiar los PS (ya está al máximo)
        normalPotion.usar(pokemon);
        assertEquals(100, pokemon.getPs());
        
        superPotion.usar(pokemon);
        assertEquals(100, pokemon.getPs());
        
        hyperPotion.usar(pokemon);
        assertEquals(100, pokemon.getPs());
    }
    
    @Test
    public void testPotionCombination() throws POOBkemonException {
        // Dañar al Pokémon severamente
        pokemon.recibirDaño(90);
        assertEquals(10, pokemon.getPs());
        
        // Usar varias pociones en secuencia
        normalPotion.usar(pokemon); // +20 PS
        assertEquals(30, pokemon.getPs());
        
        superPotion.usar(pokemon); // +50 PS
        assertEquals(80, pokemon.getPs());
        
        hyperPotion.usar(pokemon); // +100 PS (pero tope en 100)
        assertEquals(100, pokemon.getPs());
    }
    
    @Test
    public void testItemsNombresYDescripciones() {
        assertEquals("Poción Normal", normalPotion.getNombre());
        assertEquals("Super Poción", superPotion.getNombre());
        assertEquals("Hiper Poción", hyperPotion.getNombre());
        assertEquals("Revivir", revive.getNombre());
        
        // Verificar que las descripciones no son nulas
        assertNotNull(normalPotion.getDescripcion());
        assertNotNull(superPotion.getDescripcion());
        assertNotNull(hyperPotion.getDescripcion());
        assertNotNull(revive.getDescripcion());
        
        // Verificar contenido básico de las descripciones
        assertTrue(normalPotion.getDescripcion().contains("20"));
        assertTrue(superPotion.getDescripcion().contains("50"));
        assertTrue(hyperPotion.getDescripcion().contains("100") || hyperPotion.getDescripcion().contains("200"));
        assertTrue(revive.getDescripcion().toLowerCase().contains("revive") || 
                  revive.getDescripcion().toLowerCase().contains("debilitado"));
    }
    
    @Test
    public void testCantidadesCuracion() {
        // Verificar las cantidades específicas de curación
        assertEquals(20, normalPotion.getCantidadCuracion());
        assertEquals(50, superPotion.getCantidadCuracion());
        assertEquals(100, hyperPotion.getCantidadCuracion());
    }
    
    @Test
    public void testCuracionParcial() throws POOBkemonException {
        // Probar curaciones que no lleguen al máximo
        pokemon.recibirDaño(15); // 85 PS
        normalPotion.usar(pokemon); // +20 PS = 105, pero máximo 100
        assertEquals(100, pokemon.getPs());
        
        // Probar con super poción
        pokemon.recibirDaño(30); // 70 PS
        superPotion.usar(pokemon); // +50 PS = 120, pero máximo 100
        assertEquals(100, pokemon.getPs());
        
        // Probar sin exceder el máximo
        pokemon.recibirDaño(25); // 75 PS
        normalPotion.usar(pokemon); // +20 PS = 95
        assertEquals(95, pokemon.getPs());
    }
}