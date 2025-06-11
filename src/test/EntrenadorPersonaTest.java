package test;

import domain.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EntrenadorPersonaTest {
    
    private EntrenadorPersona entrenador;
    private Pokemon charmander;
    private Pokemon squirtle;
    private Pokemon bulbasaur;
    private List<Movimiento> movimientos;
    
    @Before
    public void setUp() {
        // Crear entrenador
        entrenador = new EntrenadorPersona("Ash");
        
        // Crear movimientos
        movimientos = new ArrayList<>();
        movimientos.add(new MovimientoFisico(
            "Arañazo", new Tipo("Normal"), 40, 100, 35, 35, false, "Físico", "Ataque básico"
        ));
        movimientos.add(new MovimientoEspecial(
            "Lanzallamas", new Tipo("Fire"), 90, 100, 15, 15, false, "Especial", "Ataque de fuego"
        ));
        movimientos.add(new MovimientoEstado(
            "Refugio", new Tipo("Normal"), 0, 100, 20, 20, false, "Estado", "Aumenta defensa"
        ));
        
        // Crear Pokémon
        charmander = new Pokemon(
            100, 100, 80, 70, 65, 85, 65, "Charmander", new Tipo("Fire"), null, movimientos
        );
        
        squirtle = new Pokemon(
            50, 100, 70, 80, 55, 70, 75, "Squirtle", new Tipo("Water"), null, movimientos
        );
        
        bulbasaur = new Pokemon(
            0, 100, 75, 75, 60, 80, 80, "Bulbasaur", new Tipo("Grass"), null, movimientos
        );
        
        // Agregar Pokémon al entrenador
        entrenador.agregarPokemon(charmander);
        entrenador.agregarPokemon(squirtle);
        entrenador.agregarPokemon(bulbasaur);
        
        // Agregar items
        entrenador.agregarItem(new NormalPotion());
        entrenador.agregarItem(new SuperPotion());
        entrenador.agregarItem(new HyperPotion());
        entrenador.agregarItem(new Revive());
    }
    
    @Test
    public void testConstructor() {
        assertEquals("Ash", entrenador.getNombre());
        assertNotNull(entrenador.getPokemones());
        assertNotNull(entrenador.getItems());
        assertTrue(entrenador.getPokemones().size() > 0);
        assertTrue(entrenador.getItems().size() > 0);
    }
    
    @Test
    public void testElegirAccion() throws POOBkemonException {
        // Simular entrada del usuario: elegir atacar (opción 1)
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        EntrenadorPersona oponente = new EntrenadorPersona("Gary");
        oponente.agregarPokemon(new Pokemon(100, 100, 70, 70, 60, 70, 70, "Pikachu", new Tipo("Electric"), null, movimientos));
        
        int accion = entrenador.elegirAccion(oponente, scanner);
        assertEquals(1, accion);
        
        scanner.close();
    }
    
    @Test
    public void testElegirAccionCambiarPokemon() throws POOBkemonException {
        // Simular entrada del usuario: cambiar Pokémon (opción 2)
        String input = "2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        EntrenadorPersona oponente = new EntrenadorPersona("Gary");
        oponente.agregarPokemon(new Pokemon(100, 100, 70, 70, 60, 70, 70, "Pikachu", new Tipo("Electric"), null, movimientos));
        
        int accion = entrenador.elegirAccion(oponente, scanner);
        assertEquals(2, accion);
        
        scanner.close();
    }
    
    @Test
    public void testElegirAccionUsarObjeto() throws POOBkemonException {
        // Simular entrada del usuario: usar objeto (opción 3)
        String input = "3\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        EntrenadorPersona oponente = new EntrenadorPersona("Gary");
        oponente.agregarPokemon(new Pokemon(100, 100, 70, 70, 60, 70, 70, "Pikachu", new Tipo("Electric"), null, movimientos));
        
        int accion = entrenador.elegirAccion(oponente, scanner);
        assertEquals(3, accion);
        
        scanner.close();
    }
    
    @Test
    public void testElegirAccionHuir() throws POOBkemonException {
        // Simular entrada del usuario: huir (opción 4)
        String input = "4\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        EntrenadorPersona oponente = new EntrenadorPersona("Gary");
        oponente.agregarPokemon(new Pokemon(100, 100, 70, 70, 60, 70, 70, "Pikachu", new Tipo("Electric"), null, movimientos));
        
        int accion = entrenador.elegirAccion(oponente, scanner);
        assertEquals(4, accion);
        
        scanner.close();
    }
    
    @Test(expected = POOBkemonException.class)
    public void testElegirAccionInvalida() throws POOBkemonException {
        // Simular entrada inválida seguida de entrada válida
        String input = "5\n1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        EntrenadorPersona oponente = new EntrenadorPersona("Gary");
        oponente.agregarPokemon(new Pokemon(100, 100, 70, 70, 60, 70, 70, "Pikachu", new Tipo("Electric"), null, movimientos));
        
        entrenador.elegirAccion(oponente, scanner);
        scanner.close();
    }
    
    @Test
    public void testElegirPokemon() throws POOBkemonException {
        // Simular selección del primer Pokémon (Charmander)
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        entrenador.setScanner(scanner);
        
        Pokemon pokemonElegido = entrenador.elegirPokemon();
        assertEquals("Charmander", pokemonElegido.getName());
        assertEquals(charmander, pokemonElegido);
        
        scanner.close();
    }
    
    @Test
    public void testElegirPokemonSegundo() throws POOBkemonException {
        // Simular selección del segundo Pokémon (Squirtle)
        String input = "2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        entrenador.setScanner(scanner);
        
        Pokemon pokemonElegido = entrenador.elegirPokemon();
        assertEquals("Squirtle", pokemonElegido.getName());
        assertEquals(squirtle, pokemonElegido);
        
        scanner.close();
    }
    
    @Test(expected = POOBkemonException.class)
    public void testElegirPokemonDebilitado() throws POOBkemonException {
        // Simular selección del tercer Pokémon (Bulbasaur, que está debilitado)
        String input = "3\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        entrenador.setScanner(scanner);
        
        entrenador.elegirPokemon();
        scanner.close();
    }
    
    @Test(expected = POOBkemonException.class)
    public void testElegirPokemonIndiceInvalido() throws POOBkemonException {
        // Simular selección de índice inválido
        String input = "5\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        entrenador.setScanner(scanner);
        
        entrenador.elegirPokemon();
        scanner.close();
    }
    
    @Test
    public void testElegirMovimiento() throws POOBkemonException {
        // Simular selección del primer movimiento
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        Movimiento movimientoElegido = entrenador.elegirMovimiento(charmander, scanner);
        assertEquals("Arañazo", movimientoElegido.getNombre());
        
        scanner.close();
    }
    
    @Test
    public void testElegirMovimientoSegundo() throws POOBkemonException {
        // Simular selección del segundo movimiento
        String input = "2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        Movimiento movimientoElegido = entrenador.elegirMovimiento(charmander, scanner);
        assertEquals("Lanzallamas", movimientoElegido.getNombre());
        
        scanner.close();
    }
    
    @Test(expected = POOBkemonException.class)
    public void testElegirMovimientoSinPP() throws POOBkemonException {
        // Agotar PP del primer movimiento
        charmander.getMovimientos().get(0).setPP(0);
        
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        entrenador.elegirMovimiento(charmander, scanner);
        scanner.close();
    }
    
    @Test
    public void testElegirItem() throws POOBkemonException {
        // Simular selección del primer item
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        entrenador.setScanner(scanner);
        
        Item itemElegido = entrenador.elegirItem();
        assertTrue(itemElegido instanceof NormalPotion);
        
        scanner.close();
    }
    
    @Test
    public void testElegirItemSegundo() throws POOBkemonException {
        // Simular selección del segundo item
        String input = "2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        entrenador.setScanner(scanner);
        
        Item itemElegido = entrenador.elegirItem();
        assertTrue(itemElegido instanceof SuperPotion);
        
        scanner.close();
    }
    
    @Test(expected = POOBkemonException.class)
    public void testElegirItemSinItems() throws POOBkemonException {
        // Crear entrenador sin items
        EntrenadorPersona entrenadorSinItems = new EntrenadorPersona("Gary");
        
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        entrenadorSinItems.setScanner(scanner);
        
        entrenadorSinItems.elegirItem();
        scanner.close();
    }
    
    @Test
    public void testElegirPokemonParaItem() throws POOBkemonException {
        // Simular selección del primer Pokémon para usar item
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        entrenador.setScanner(scanner);
        
        Pokemon pokemonElegido = entrenador.elegirPokemonParaItem();
        assertEquals("Charmander", pokemonElegido.getName());
        
        scanner.close();
    }
    
    @Test
    public void testDecidirHuirSi() throws POOBkemonException {
        // Simular decisión de huir (opción 1 = Sí)
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        EntrenadorPersona oponente = new EntrenadorPersona("Gary");
        boolean decision = entrenador.decidirHuir(oponente, scanner);
        assertTrue(decision);
        
        scanner.close();
    }
    
    @Test
    public void testDecidirHuirNo() throws POOBkemonException {
        // Simular decisión de no huir (opción 2 = No)
        String input = "2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        EntrenadorPersona oponente = new EntrenadorPersona("Gary");
        boolean decision = entrenador.decidirHuir(oponente, scanner);
        assertFalse(decision);
        
        scanner.close();
    }
    
    @Test(expected = POOBkemonException.class)
    public void testDecidirHuirOpcionInvalida() throws POOBkemonException {
        // Simular decisión inválida
        String input = "3\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        EntrenadorPersona oponente = new EntrenadorPersona("Gary");
        entrenador.decidirHuir(oponente, scanner);
        scanner.close();
    }
    
    @Test
    public void testGettersYSetters() {
        // Test de getters y setters para los atributos de elección
        assertNull(entrenador.getPokemonElegido());
        assertNull(entrenador.getMovimientoElegido());
        assertNull(entrenador.getItemElegido());
        
        entrenador.setPokemonElegido(charmander);
        entrenador.setMovimientoElegido(movimientos.get(0));
        entrenador.setItemElegido(new NormalPotion());
        
        assertEquals(charmander, entrenador.getPokemonElegido());
        assertEquals(movimientos.get(0), entrenador.getMovimientoElegido());
        assertTrue(entrenador.getItemElegido() instanceof NormalPotion);
    }
    
    @Test
    public void testPuedeCombatir() {
        // Con Pokémon disponibles
        assertTrue(entrenador.puedeCombatir());
        
        // Debilitar todos los Pokémon
        charmander.recibirDaño(100);
        squirtle.recibirDaño(50);
        bulbasaur.recibirDaño(0); // Ya está debilitado
        
        assertFalse(entrenador.puedeCombatir());
    }
    
    @Test
    public void testGetPrimerPokemonDisponible() {
        // Debería devolver Charmander (primer Pokémon disponible)
        Pokemon primerDisponible = entrenador.getPrimerPokemonDisponible();
        assertEquals("Charmander", primerDisponible.getName());
        
        // Debilitar Charmander
        charmander.recibirDaño(100);
        
        // Ahora debería devolver Squirtle
        primerDisponible = entrenador.getPrimerPokemonDisponible();
        assertEquals("Squirtle", primerDisponible.getName());
        
        // Debilitar Squirtle también
        squirtle.recibirDaño(50);
        
        // No debería haber ningún Pokémon disponible
        primerDisponible = entrenador.getPrimerPokemonDisponible();
        assertNull(primerDisponible);
    }
    
    @Test
    public void testGetEstadoEquipo() {
        String estado = entrenador.getEstadoEquipo();
        
        assertTrue(estado.contains("Ash"));
        assertTrue(estado.contains("Charmander"));
        assertTrue(estado.contains("Squirtle"));
        assertTrue(estado.contains("Bulbasaur"));
        assertTrue(estado.contains("PS"));
        assertTrue(estado.contains("DEBILITADO")); // Bulbasaur está debilitado
    }
    
    @Test
    public void testToString() {
        String resultado = entrenador.toString();
        assertTrue(resultado.contains("Entrenador Persona"));
    }
}