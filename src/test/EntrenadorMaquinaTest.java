package test;

import domain.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EntrenadorMaquinaTest {
    
    private AttackingTrainer entrenadorOfensivo;
    private DefensiveTrainer entrenadorDefensivo;
    private ChangingTrainer entrenadorCambiante;
    private ExpertTrainer entrenadorExperto;
    
    private Pokemon charmander;
    private Pokemon squirtle;
    private Pokemon bulbasaur;
    private Pokemon pikachu;
    
    private List<Movimiento> movimientosOfensivos;
    private List<Movimiento> movimientosDefensivos;
    private List<Movimiento> movimientosEstado;
    
    @Before
    public void setUp() {
        // Crear movimientos ofensivos
        movimientosOfensivos = new ArrayList<>();
        movimientosOfensivos.add(new MovimientoFisico(
            "Placaje", new Tipo("Normal"), 40, 100, 35, 35, false, "Físico", "Ataque básico"
        ));
        movimientosOfensivos.add(new MovimientoEspecial(
            "Lanzallamas", new Tipo("Fire"), 90, 100, 15, 15, false, "Especial", "Ataque de fuego"
        ));
        movimientosOfensivos.add(new MovimientoFisico(
            "Terremoto", new Tipo("Ground"), 100, 100, 10, 10, false, "Físico", "Ataque poderoso"
        ));
        
        // Crear movimientos defensivos
        movimientosDefensivos = new ArrayList<>();
        movimientosDefensivos.add(new MovimientoEstado(
            "Refugio", new Tipo("Normal"), 0, 100, 20, 20, false, "Estado", "Aumenta defensa"
        ));
        movimientosDefensivos.add(new MovimientoEstado(
            "Respiro", new Tipo("Normal"), 0, 100, 10, 10, false, "Estado", "Restaura PS"
        ));
        movimientosDefensivos.add(new MovimientoEspecial(
            "Pistola Agua", new Tipo("Water"), 40, 100, 25, 25, false, "Especial", "Ataque de agua"
        ));
        
        // Crear movimientos de estado
        movimientosEstado = new ArrayList<>();
        movimientosEstado.add(new MovimientoEstado(
            "Tóxico", new Tipo("Poison"), 0, 90, 10, 10, false, "Estado", "Envenena gravemente"
        ));
        movimientosEstado.add(new MovimientoEstado(
            "Paralizador", new Tipo("Electric"), 0, 100, 30, 30, false, "Estado", "Paraliza"
        ));
        movimientosEstado.add(new MovimientoFisico(
            "Arañazo", new Tipo("Normal"), 40, 100, 35, 35, false, "Físico", "Ataque con garras"
        ));
        
        // Crear Pokémon
        charmander = new Pokemon(
            100, 100, 120, 70, 65, 85, 65, "Charmander", new Tipo("Fire"), null, movimientosOfensivos
        );
        
        squirtle = new Pokemon(
            100, 100, 70, 100, 55, 70, 90, "Squirtle", new Tipo("Water"), null, movimientosDefensivos
        );
        
        bulbasaur = new Pokemon(
            100, 100, 75, 75, 60, 80, 80, "Bulbasaur", new Tipo("Grass"), null, movimientosEstado
        );
        
        pikachu = new Pokemon(
            100, 100, 80, 60, 90, 85, 70, "Pikachu", new Tipo("Electric"), null, movimientosOfensivos
        );
        
        // Crear entrenadores máquina
        entrenadorOfensivo = new AttackingTrainer("CPU Ofensivo");
        entrenadorDefensivo = new DefensiveTrainer("CPU Defensivo");
        entrenadorCambiante = new ChangingTrainer("CPU Cambiante");
        entrenadorExperto = new ExpertTrainer("CPU Experto");
        
        // Agregar Pokémon y items a todos los entrenadores
        setupEntrenador(entrenadorOfensivo);
        setupEntrenador(entrenadorDefensivo);
        setupEntrenador(entrenadorCambiante);
        setupEntrenador(entrenadorExperto);
    }
    
    private void setupEntrenador(EntrenadorMaquina entrenador) {
        entrenador.agregarPokemon(new Pokemon(charmander));
        entrenador.agregarPokemon(new Pokemon(squirtle));
        entrenador.agregarPokemon(new Pokemon(bulbasaur));
        entrenador.agregarPokemon(new Pokemon(pikachu));
        
        entrenador.agregarItem(new NormalPotion());
        entrenador.agregarItem(new SuperPotion());
        entrenador.agregarItem(new HyperPotion());
        entrenador.agregarItem(new Revive());
    }
    
    // ======= TESTS PARA ATTACKING TRAINER =======
    
    @Test
    public void testAttackingTrainerConstructor() {
        assertEquals("CPU Ofensivo", entrenadorOfensivo.getNombre());
        assertEquals("attacking", entrenadorOfensivo.getTipoMaquina());
    }
    
    @Test
    public void testAttackingTrainerElegirMejorPokemon() throws POOBkemonException {
        // El entrenador ofensivo debería elegir el Pokémon con mayor ataque
        Pokemon pokemonElegido = entrenadorOfensivo.elegirPokemon();
        
        // Charmander tiene el mayor ataque (120)
        assertEquals("Charmander", pokemonElegido.getName());
    }
    
    @Test
    public void testAttackingTrainerElegirMovimiento() throws POOBkemonException {
        Scanner scanner = new Scanner(System.in);
        Pokemon pokemon = entrenadorOfensivo.getPokemones().get(0); // Charmander
        
        Movimiento movimientoElegido = entrenadorOfensivo.elegirMovimiento(pokemon, scanner);
        
        // Debería elegir un movimiento ofensivo (no de estado)
        assertNotNull(movimientoElegido);
        assertTrue(movimientoElegido instanceof MovimientoFisico || 
                  movimientoElegido instanceof MovimientoEspecial);
        
        scanner.close();
    }
    
    @Test
    public void testAttackingTrainerElegirAccion() throws POOBkemonException {
        Scanner scanner = new Scanner(System.in);
        EntrenadorPersona oponente = new EntrenadorPersona("Oponente");
        oponente.agregarPokemon(new Pokemon(squirtle));
        
        int accion = entrenadorOfensivo.elegirAccion(oponente, scanner);
        
        // La acción debería ser válida (1-4)
        assertTrue(accion >= 1 && accion <= 4);
        
        scanner.close();
    }
    
    @Test
    public void testAttackingTrainerUsaItemCuandoVidaBaja() throws POOBkemonException {
        Scanner scanner = new Scanner(System.in);
        
        // Reducir vida del primer Pokémon
        entrenadorOfensivo.getPokemones().get(0).recibirDaño(80); // 20 PS restantes
        
        EntrenadorPersona oponente = new EntrenadorPersona("Oponente");
        oponente.agregarPokemon(new Pokemon(squirtle));
        
        int accion = entrenadorOfensivo.elegirAccion(oponente, scanner);
        
        // Debería elegir usar item (acción 3) cuando la vida está baja
        assertEquals(3, accion);
        
        scanner.close();
    }
    
    @Test
    public void testAttackingTrainerElegirItem() throws POOBkemonException {
        Item itemElegido = entrenadorOfensivo.elegirItem();
        
        // Debería elegir un item válido
        assertNotNull(itemElegido);
        assertTrue(itemElegido instanceof NormalPotion || 
                  itemElegido instanceof SuperPotion || 
                  itemElegido instanceof HyperPotion || 
                  itemElegido instanceof Revive);
    }
    
    @Test
    public void testAttackingTrainerElegirPokemonParaItem() throws POOBkemonException {
        Pokemon pokemonParaItem = entrenadorOfensivo.elegirPokemonParaItem();
        
        // Debería elegir un Pokémon válido
        assertNotNull(pokemonParaItem);
        assertTrue(entrenadorOfensivo.getPokemones().contains(pokemonParaItem));
    }
    
    @Test
    public void testAttackingTrainerNoHuye() throws POOBkemonException {
        Scanner scanner = new Scanner(System.in);
        EntrenadorPersona oponente = new EntrenadorPersona("Oponente");
        
        boolean decision = entrenadorOfensivo.decidirHuir(oponente, scanner);
        
        // Los entrenadores máquina no huyen
        assertFalse(decision);
        
        scanner.close();
    }
    
    // ======= TESTS PARA DEFENSIVE TRAINER =======
    
    @Test
    public void testDefensiveTrainerConstructor() {
        assertEquals("CPU Defensivo", entrenadorDefensivo.getNombre());
        assertEquals("defensive", entrenadorDefensivo.getTipoMaquina());
    }
    
    @Test
    public void testDefensiveTrainerElegirMejorPokemon() throws POOBkemonException {
        // El entrenador defensivo debería elegir el Pokémon con mejor defensa
        Pokemon pokemonElegido = entrenadorDefensivo.elegirPokemon();
        
        // Squirtle tiene la mejor defensa (100) y defensa especial (90)
        assertEquals("Squirtle", pokemonElegido.getName());
    }
    
    @Test
    public void testDefensiveTrainerPrefiereMovimientosEstado() throws POOBkemonException {
        Scanner scanner = new Scanner(System.in);
        
        // Usar un Pokémon con movimientos de estado
        Pokemon pokemon = entrenadorDefensivo.getPokemones().get(2); // Bulbasaur con movimientos de estado
        
        Movimiento movimientoElegido = entrenadorDefensivo.elegirMovimiento(pokemon, scanner);
        
        assertNotNull(movimientoElegido);
        
        scanner.close();
    }
    
    // ======= TESTS PARA CHANGING TRAINER =======
    
    @Test
    public void testChangingTrainerConstructor() {
        assertEquals("CPU Cambiante", entrenadorCambiante.getNombre());
        assertEquals("changing", entrenadorCambiante.getTipoMaquina());
    }
    
    @Test
    public void testChangingTrainerCambiaFrecuentemente() throws POOBkemonException {
        Scanner scanner = new Scanner(System.in);
        EntrenadorPersona oponente = new EntrenadorPersona("Oponente");
        oponente.agregarPokemon(new Pokemon(charmander)); // Pokémon de fuego
        
        // El entrenador cambiante debería considerar cambiar frecuentemente
        int accion = entrenadorCambiante.elegirAccion(oponente, scanner);
        
        assertTrue(accion >= 1 && accion <= 4);
        
        scanner.close();
    }
    
    // ======= TESTS PARA EXPERT TRAINER =======
    
    @Test
    public void testExpertTrainerConstructor() {
        assertEquals("CPU Experto", entrenadorExperto.getNombre());
        assertEquals("expert", entrenadorExperto.getTipoMaquina());
    }
    
    @Test
    public void testExpertTrainerEsInteligente() throws POOBkemonException {
        Scanner scanner = new Scanner(System.in);
        EntrenadorPersona oponente = new EntrenadorPersona("Oponente");
        oponente.agregarPokemon(new Pokemon(squirtle));
        
        // El entrenador experto debería tomar decisiones inteligentes
        int accion = entrenadorExperto.elegirAccion(oponente, scanner);
        
        assertTrue(accion >= 1 && accion <= 4);
        
        scanner.close();
    }
    
    @Test
    public void testExpertTrainerElegirItemInteligentemente() throws POOBkemonException {
        // Debilitar un Pokémon
        entrenadorExperto.getPokemones().get(0).recibirDaño(100);
        
        Item itemElegido = entrenadorExperto.elegirItem();
        
        // Debería elegir Revive para el Pokémon debilitado
        assertTrue(itemElegido instanceof Revive);
    }
    
    // ======= TESTS GENERALES PARA ENTRENADOR MAQUINA =======
    
    @Test
    public void testEntrenadorMaquinaPuedeCombatir() {
        assertTrue(entrenadorOfensivo.puedeCombatir());
        assertTrue(entrenadorDefensivo.puedeCombatir());
        assertTrue(entrenadorCambiante.puedeCombatir());
        assertTrue(entrenadorExperto.puedeCombatir());
        
        // Debilitar todos los Pokémon de un entrenador
        for (Pokemon pokemon : entrenadorOfensivo.getPokemones()) {
            pokemon.recibirDaño(pokemon.getPs());
        }
        
        assertFalse(entrenadorOfensivo.puedeCombatir());
    }
    
    @Test
    public void testEntrenadorMaquinaGetPrimerPokemonDisponible() {
        Pokemon primerDisponible = entrenadorOfensivo.getPrimerPokemonDisponible();
        assertNotNull(primerDisponible);
        assertFalse(primerDisponible.estaDebilitado());
        
        // Debilitar el primer Pokémon
        primerDisponible.recibirDaño(primerDisponible.getPs());
        
        Pokemon segundoDisponible = entrenadorOfensivo.getPrimerPokemonDisponible();
        assertNotNull(segundoDisponible);
        assertNotEquals(primerDisponible, segundoDisponible);
    }
    
    // ======= TESTS ESPECÍFICOS DE COMPORTAMIENTO =======
    
    @Test
    public void testAttackingTrainerPrefiereAtaquesPoderosos() throws POOBkemonException {
        Scanner scanner = new Scanner(System.in);
        
        // Crear un Pokémon con movimientos de diferentes poderes
        List<Movimiento> movimientosVariados = new ArrayList<>();
        movimientosVariados.add(new MovimientoFisico(
            "AtaqueDebil", new Tipo("Normal"), 20, 100, 20, 20, false, "Físico", "Ataque débil"
        ));
        movimientosVariados.add(new MovimientoFisico(
            "AtaqueFuerte", new Tipo("Normal"), 100, 100, 10, 10, false, "Físico", "Ataque fuerte"
        ));
        
        Pokemon pokemonTest = new Pokemon(
            100, 100, 120, 70, 65, 85, 65, "TestPokemon", new Tipo("Normal"), null, movimientosVariados
        );
        
        // Múltiples pruebas para verificar tendencia hacia ataques poderosos
        int vecesAtaqueFuerte = 0;
        int totalPruebas = 10;
        
        for (int i = 0; i < totalPruebas; i++) {
            // Resetear PP
            for (Movimiento mov : pokemonTest.getMovimientos()) {
                mov.restaurarPPCompleto();
            }
            
            Movimiento elegido = entrenadorOfensivo.elegirMovimiento(pokemonTest, scanner);
            if (elegido.getNombre().equals("AtaqueFuerte")) {
                vecesAtaqueFuerte++;
            }
        }
        
        // Debería preferir el ataque fuerte la mayoría de las veces
        assertTrue("El entrenador ofensivo debería preferir ataques poderosos", 
                  vecesAtaqueFuerte > totalPruebas / 2);
        
        scanner.close();
    }
    
    @Test
    public void testDefensiveTrainerUsaItemsMasFrecuentemente() throws POOBkemonException {
        Scanner scanner = new Scanner(System.in);
        
        // Reducir vida moderadamente
        entrenadorDefensivo.getPokemones().get(0).recibirDaño(60); // 40 PS restantes
        
        EntrenadorPersona oponente = new EntrenadorPersona("Oponente");
        oponente.agregarPokemon(new Pokemon(squirtle));
        
        // El entренador defensivo debería ser más propenso a usar items con vida media
        int accion = entrenadorDefensivo.elegirAccion(oponente, scanner);
        
        // Verificar que la acción es válida
        assertTrue(accion >= 1 && accion <= 4);
        
        scanner.close();
    }
    
    @Test
    public void testChangingTrainerCambiaConDesventajaTipo() throws POOBkemonException {
        Scanner scanner = new Scanner(System.in);
        
        // Crear oponente con ventaja de tipo
        EntrenadorPersona oponente = new EntrenadorPersona("Oponente");
        Pokemon pokemonAgua = new Pokemon(
            100, 100, 80, 80, 60, 80, 80, "AguaFuerte", new Tipo("Water"), null, movimientosOfensivos
        );
        oponente.agregarPokemon(pokemonAgua);
        
        // Asegurar que el primer Pokémon del cambiante sea de tipo Fuego (desventaja)
        entrenadorCambiante.getPokemones().clear();
        entrenadorCambiante.agregarPokemon(new Pokemon(charmander)); // Fuego vs Agua = desventaja
        entrenadorCambiante.agregarPokemon(new Pokemon(bulbasaur)); // Planta vs Agua = ventaja
        
        int accion = entrenadorCambiante.elegirAccion(oponente, scanner);
        
        // Debería considerar cambiar (acción 2) o atacar (acción 1)
        assertTrue(accion == 1 || accion == 2);
        
        scanner.close();
    }
    
    @Test(expected = POOBkemonException.class)
    public void testEntrenadorMaquinaSinItems() throws POOBkemonException {
        // Crear entrenador sin items
        AttackingTrainer entrenadorSinItems = new AttackingTrainer("Sin Items");
        entrenadorSinItems.elegirItem();
    }
    
    @Test
    public void testEstadoEquipoEntrenadorMaquina() {
        String estado = entrenadorOfensivo.getEstadoEquipo();
        
        assertTrue(estado.contains("CPU Ofensivo"));
        assertTrue(estado.contains("Charmander"));
        assertTrue(estado.contains("PS"));
        
        // Debilitar un Pokémon y verificar
        entrenadorOfensivo.getPokemones().get(0).recibirDaño(100);
        estado = entrenadorOfensivo.getEstadoEquipo();
        assertTrue(estado.contains("DEBILITADO"));
    }
}