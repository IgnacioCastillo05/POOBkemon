package test;

import domain.Pokemon;
import domain.Tipo;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class TipoTest {
    
    private Tipo tipoFuego;
    private Tipo tipoAgua;
    private Tipo tipoPlanta;
    private Tipo tipoElectrico;
    private Tipo tipoNormal;
    private Tipo tipoRoca;
    private Tipo tipoAcero;
    private Tipo tipoFantasma;
    private Tipo tipoTierra;
    private Tipo tipoHielo;
    private Tipo tipoLucha;
    
    private Pokemon pokemonFuego;
    private Pokemon pokemonAgua;
    private Pokemon pokemonPlanta;
    private Pokemon pokemonDobleTipo;
    
    @Before
    public void setUp() {
        // Crear tipos usando los nombres en inglés (como en la tabla de efectividades)
        tipoFuego = new Tipo("Fire");
        tipoAgua = new Tipo("Water");
        tipoPlanta = new Tipo("Grass");
        tipoElectrico = new Tipo("Electric");
        tipoNormal = new Tipo("Normal");
        tipoRoca = new Tipo("Rock");
        tipoAcero = new Tipo("Steel");
        tipoFantasma = new Tipo("Ghost");
        tipoTierra = new Tipo("Ground");
        tipoHielo = new Tipo("Ice");
        tipoLucha = new Tipo("Fighting");
        
        // Crear Pokémon de diferentes tipos
        pokemonFuego = new Pokemon(
            100, 100, 80, 70, 60, 75, 65, "Charmander", tipoFuego, null, new ArrayList<>()
        );
        
        pokemonAgua = new Pokemon(
            100, 100, 70, 65, 50, 65, 70, "Squirtle", tipoAgua, null, new ArrayList<>()
        );
        
        pokemonPlanta = new Pokemon(
            100, 100, 70, 75, 45, 65, 75, "Bulbasaur", tipoPlanta, null, new ArrayList<>()
        );
        
        // Pokémon con doble tipo: Agua/Volador (como Gyarados en los juegos)
        pokemonDobleTipo = new Pokemon(
            100, 100, 75, 80, 55, 70, 75, "Gyarados", tipoAgua, new Tipo("Flying"), new ArrayList<>()
        );
    }
    
    @Test
    public void testEfectividadesBasicas() {
        // Fuego vs Agua (no muy efectivo)
        assertEquals(0.5, tipoFuego.calcularEfectividad(tipoAgua), 0.001);
        
        // Fuego vs Planta (súper efectivo)
        assertEquals(2.0, tipoFuego.calcularEfectividad(tipoPlanta), 0.001);
        
        // Fuego vs Fuego (no muy efectivo)
        assertEquals(0.5, tipoFuego.calcularEfectividad(tipoFuego), 0.001);
        
        // Agua vs Fuego (súper efectivo)
        assertEquals(2.0, tipoAgua.calcularEfectividad(tipoFuego), 0.001);
        
        // Planta vs Agua (súper efectivo)
        assertEquals(2.0, tipoPlanta.calcularEfectividad(tipoAgua), 0.001);
        
        // Eléctrico vs Agua (súper efectivo)
        assertEquals(2.0, tipoElectrico.calcularEfectividad(tipoAgua), 0.001);
        
        // Normal vs cualquier tipo (efectividad normal por defecto)
        assertEquals(1.0, tipoNormal.calcularEfectividad(tipoFuego), 0.001);
        assertEquals(1.0, tipoNormal.calcularEfectividad(tipoAgua), 0.001);
        assertEquals(1.0, tipoNormal.calcularEfectividad(tipoPlanta), 0.001);
    }
    
    @Test
    public void testInmunidades() {
        // Normal vs Fantasma (inmune)
        assertEquals(0.0, tipoNormal.calcularEfectividad(tipoFantasma), 0.001);
        
        // Fantasma vs Normal (inmune)
        assertEquals(0.0, tipoFantasma.calcularEfectividad(tipoNormal), 0.001);
        
        // Eléctrico vs Tierra (inmune)
        assertEquals(0.0, tipoElectrico.calcularEfectividad(tipoTierra), 0.001);
        
        // Lucha vs Fantasma (inmune)
        assertEquals(0.0, tipoLucha.calcularEfectividad(tipoFantasma), 0.001);
    }
    
    @Test
    public void testEfectividadContraPokemon() {
        // Fuego vs Pokémon de tipo Agua (no muy efectivo)
        assertEquals(0.5, tipoFuego.calcularEfectividadContraPokemon(pokemonAgua), 0.001);
        
        // Agua vs Pokémon de tipo Fuego (súper efectivo)
        assertEquals(2.0, tipoAgua.calcularEfectividadContraPokemon(pokemonFuego), 0.001);
        
        // Planta vs Pokémon de tipo Agua (súper efectivo)
        assertEquals(2.0, tipoPlanta.calcularEfectividadContraPokemon(pokemonAgua), 0.001);
    }
    
    @Test
    public void testEfectividadTipoDual() {
        // Eléctrico vs Pokémon Agua/Volador (doble súper efectivo = 4x)
        // Eléctrico es súper efectivo contra Agua (2x) y Volador (2x) = 2x * 2x = 4x
        assertEquals(4.0, tipoElectrico.calcularEfectividadContraPokemon(pokemonDobleTipo), 0.001);
        
        // Roca vs Pokémon Agua/Volador
        // Roca es súper efectivo contra Volador (2x) pero normal contra Agua (1x) = 2x * 1x = 2x
        assertEquals(2.0, tipoRoca.calcularEfectividadContraPokemon(pokemonDobleTipo), 0.001);
        
        // Planta vs Pokémon Agua/Volador
        // Planta es súper efectivo contra Agua (2x) pero no muy efectivo contra Volador (0.5x) = 2x * 0.5x = 1x
        assertEquals(1.0, tipoPlanta.calcularEfectividadContraPokemon(pokemonDobleTipo), 0.001);
    }
    
    @Test
    public void testEfectividadTipoNoRegistrado() {
        // Crear un tipo que no está registrado en la tabla de efectividades
        Tipo tipoNoRegistrado = new Tipo("TipoInventado");
        
        // La efectividad debería ser neutral (1.0) para tipos no registrados
        assertEquals(1.0, tipoNoRegistrado.calcularEfectividad(tipoFuego), 0.001);
        assertEquals(1.0, tipoFuego.calcularEfectividad(tipoNoRegistrado), 0.001);
    }
    
    @Test
    public void testToString() {
        assertEquals("Fire", tipoFuego.toString());
        assertEquals("Water", tipoAgua.toString());
        assertEquals("Electric", tipoElectrico.toString());
    }
    
    @Test
    public void testEfectividadContraPokemonNulo() {
        // La efectividad contra un Pokémon nulo debería ser neutral (1.0)
        assertEquals(1.0, tipoFuego.calcularEfectividadContraPokemon(null), 0.001);
    }
    
    @Test
    public void testEfectividadContraTipoNulo() {
        // La efectividad contra un tipo nulo debería ser neutral (1.0)
        assertEquals(1.0, tipoFuego.calcularEfectividad(null), 0.001);
    }
    
    @Test
    public void testCombinacionesComplejas() {
        // Probar combinaciones más complejas basadas en la tabla real
        
        // Acero vs Fuego (no muy efectivo)
        assertEquals(0.5, tipoAcero.calcularEfectividad(tipoFuego), 0.001);
        
        // Acero vs Hielo (súper efectivo)
        assertEquals(2.0, tipoAcero.calcularEfectividad(tipoHielo), 0.001);
        
        // Acero vs Acero (no muy efectivo)
        assertEquals(0.5, tipoAcero.calcularEfectividad(tipoAcero), 0.001);
        
        // Roca vs Fuego (súper efectivo)
        assertEquals(2.0, tipoRoca.calcularEfectividad(tipoFuego), 0.001);
        
        // Roca vs Lucha (no muy efectivo)
        assertEquals(0.5, tipoRoca.calcularEfectividad(tipoLucha), 0.001);
        
        // Tierra vs Eléctrico (súper efectivo)
        assertEquals(2.0, tipoTierra.calcularEfectividad(tipoElectrico), 0.001);
        
        // Tierra vs Fuego (súper efectivo)
        assertEquals(2.0, tipoTierra.calcularEfectividad(tipoFuego), 0.001);
        
        // Hielo vs Planta (súper efectivo)
        assertEquals(2.0, tipoHielo.calcularEfectividad(tipoPlanta), 0.001);
        
        // Hielo vs Tierra (súper efectivo)
        assertEquals(2.0, tipoHielo.calcularEfectividad(tipoTierra), 0.001);
    }
    
    @Test
    public void testResistencias() {
        // Probar resistencias específicas
        
        // Fuego resiste a Fuego
        assertEquals(0.5, tipoFuego.calcularEfectividad(tipoFuego), 0.001);
        
        // Agua resiste a Agua
        assertEquals(0.5, tipoAgua.calcularEfectividad(tipoAgua), 0.001);
        
        // Planta resiste a Agua
        assertEquals(0.5, tipoPlanta.calcularEfectividad(tipoAgua), 0.001);
        
        // Eléctrico resiste a Eléctrico
        assertEquals(0.5, tipoElectrico.calcularEfectividad(tipoElectrico), 0.001);
    }
}