package domain;

import java.io.Serializable;
import java.util.Random;
import java.util.Scanner;

public class Batalla implements Serializable  {
    private Entrenador entrenador1;
    private Entrenador entrenador2;
    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private boolean turnoEntrenador1;
    private boolean batallaTeminada;
    private Random random;
    private Scanner scanner;

    private static final MovimientoForcejeo MOVIMIENTO_FORCEJEO = new MovimientoForcejeo();

    /**
     * Constructor de la clase Batalla.
     * @param entrenador1 Primer entrenador de la batalla.
     * @param entrenador2 Segundo entrenador de la batalla.
     * @param scanner Scanner para leer las entradas del usuario.
     * @throws POOBkemonException Si alguno de los entrenadores no puede combatir.
     */
    public Batalla(Entrenador entrenador1, Entrenador entrenador2, Scanner scanner) throws POOBkemonException {
        if (!entrenador1.puedeCombatir() || !entrenador2.puedeCombatir()) {
            throw new POOBkemonException("Ambos entrenadores deben tener al menos un Pokémon en condiciones de combatir.");
        }
        
        this.entrenador1 = entrenador1;
        this.entrenador2 = entrenador2;
        this.pokemon1 = entrenador1.getPrimerPokemonDisponible();
        this.pokemon2 = entrenador2.getPrimerPokemonDisponible();
        
        validarPokemonesIniciales();
        
        this.turnoEntrenador1 = true;
        this.batallaTeminada = false;
        this.random = new Random();
        this.scanner = scanner;
    }

    /**
     * Valida que los Pokémon iniciales tengan movimientos
     * @throws POOBkemonException Si algún Pokémon no tiene movimientos
     */
    private void validarPokemonesIniciales() throws POOBkemonException {
        if (pokemon1.getMovimientos() == null || pokemon1.getMovimientos().isEmpty()) {
            throw new POOBkemonException("El Pokémon " + pokemon1.getName() + " no tiene movimientos asignados.");
        }
        if (pokemon2.getMovimientos() == null || pokemon2.getMovimientos().isEmpty()) {
            throw new POOBkemonException("El Pokémon " + pokemon2.getName() + " no tiene movimientos asignados.");
        }
    }

    /**
     * Inicia la batalla y maneja el bucle principal de juego.
     * @return El entrenador ganador de la batalla.
     * @throws POOBkemonException Si ocurre algún error durante la batalla.
     */
    public Entrenador iniciarBatalla() throws POOBkemonException {
        mostrarInicioBatalla();

        while (!batallaTeminada) {
            try {
                mostrarEstadoPokemon();
                
                Entrenador ganador = procesarTurno();
                if (ganador != null) {
                    return ganador;
                }
                
                cambiarTurno();
                
            } catch (Exception e) {
                manejarErrorTurno(e);
            }
        }
        
        return determinarGanadorFinal();
    }

    /**
     * Muestra el mensaje de inicio de batalla
     */
    private void mostrarInicioBatalla() {
        System.out.println("¡Comienza la batalla entre " + entrenador1.getNombre() + " y " + entrenador2.getNombre() + "!");
        System.out.println(entrenador1.getNombre() + " envía a " + pokemon1.getName() + "!");
        System.out.println(entrenador2.getNombre() + " envía a " + pokemon2.getName() + "!");
    }

    /**
     * Procesa un turno completo de la batalla
     * @return El entrenador ganador si la batalla termina, null si continúa
     * @throws POOBkemonException Si ocurre un error durante el turno
     */
    private Entrenador procesarTurno() throws POOBkemonException {
        Entrenador entrenadorActual = turnoEntrenador1 ? entrenador1 : entrenador2;
        Entrenador entrenadorOponente = turnoEntrenador1 ? entrenador2 : entrenador1;
        Pokemon pokemonActual = turnoEntrenador1 ? pokemon1 : pokemon2;
        
        System.out.println("\nTurno de " + entrenadorActual.getNombre() + " con " + pokemonActual.getName());

        if (!aplicarEfectosEstadoInicio(pokemonActual)) {
            return null;
        }

        int accion = obtenerAccionEntrenador(entrenadorActual, entrenadorOponente);
        Entrenador ganador = ejecutarAccion(accion, entrenadorActual, entrenadorOponente, pokemonActual);
        
        if (ganador != null) {
            return ganador;
        }

        ganador = verificarCondicionesVictoria();
        if (ganador != null) {
            return ganador;
        }

        return procesarEfectosEstadoFinTurno(entrenadorActual, pokemonActual);
    }

    /**
     * Aplica efectos de estado al inicio del turno
     * @param pokemonActual El Pokémon que tiene el turno
     * @return true si el Pokémon puede actuar, false si no puede
     */
    private boolean aplicarEfectosEstadoInicio(Pokemon pokemonActual) {
        String efectoEstado = pokemonActual.aplicarEfectosEstado();
        if (!efectoEstado.isEmpty()) {
            System.out.println(efectoEstado);
        }

        boolean puedeActuar = pokemonActual.puedeAtacar();
        if (!puedeActuar) {
            System.out.println(pokemonActual.getName() + " no puede actuar debido a su estado " + pokemonActual.getStatus() + "!");
        }
        
        return puedeActuar;
    }

    /**
     * Obtiene la acción del entrenador con manejo de errores
     * @param entrenadorActual El entrenador que debe elegir
     * @param entrenadorOponente El entrenador oponente
     * @return La acción elegida
     */
    private int obtenerAccionEntrenador(Entrenador entrenadorActual, Entrenador entrenadorOponente) {
        int accion = 0;
        boolean accionValida = false;
        
        while (!accionValida) {
            try {
                accion = entrenadorActual.elegirAccion(entrenadorOponente, scanner);
                accionValida = true;
            } catch (POOBkemonException e) {
                System.out.println("Error: " + e.getMessage());
                if (entrenadorActual instanceof EntrenadorPersona) {
                    continue;
                } else {
                    accion = 1;
                    accionValida = true;
                }
            }
        }
        return accion;
    }

    /**
     * Ejecuta la acción seleccionada por el entrenador
     * @param accion La acción a ejecutar
     * @param entrenadorActual El entrenador actual
     * @param entrenadorOponente El entrenador oponente
     * @param pokemonActual El Pokémon actual
     * @return El entrenador ganador si la batalla termina por huida, null si continúa
     */
    private Entrenador ejecutarAccion(int accion, Entrenador entrenadorActual, Entrenador entrenadorOponente, Pokemon pokemonActual) {
        try {
            switch (accion) {
                case 1:
                    Pokemon pokemonOponente = turnoEntrenador1 ? pokemon2 : pokemon1;
                    realizarAtaque(entrenadorActual, pokemonActual, pokemonOponente);
                    break;
                    
                case 2:
                    cambiarPokemon(entrenadorActual);
                    break;
                    
                case 3:
                    usarObjeto(entrenadorActual);
                    break;
                    
                case 4:
                    return procesarHuida(entrenadorActual, entrenadorOponente);
                    
                default:
                    System.out.println("Acción no válida, se pierde el turno.");
                    break;
            }
        } catch (POOBkemonException e) {
            System.out.println("Error al ejecutar acción: " + e.getMessage());
            System.out.println(entrenadorActual.getNombre() + " pierde su turno.");
        }
        return null;
    }

    /**
     * Procesa el intento de huida
     * @param entrenadorActual El entrenador que intenta huir
     * @param entrenadorOponente El entrenador oponente
     * @return El entrenador ganador si se huye, null si no se huye
     */
    private Entrenador procesarHuida(Entrenador entrenadorActual, Entrenador entrenadorOponente) {
        if (entrenadorActual instanceof EntrenadorPersona) {
            EntrenadorPersona entrenadorPersona = (EntrenadorPersona) entrenadorActual;
            try {
                boolean quiereHuir = entrenadorPersona.decidirHuir(entrenadorOponente, scanner);
                if (quiereHuir) {
                    System.out.println(entrenadorActual.getNombre() + " ha huido de la batalla!");
                    batallaTeminada = true;
                    return entrenadorOponente; 
                } else {
                    System.out.println(entrenadorActual.getNombre() + " decide continuar la batalla.");
                }
            } catch (POOBkemonException e) {
                System.out.println("Error al decidir huir: " + e.getMessage());
            }
        } else {
            System.out.println(entrenadorActual.getNombre() + " ha huido de la batalla!");
            batallaTeminada = true;
            return entrenadorOponente; 
        }
        return null;
    }

    /**
     * Verifica las condiciones de victoria después de una acción
     * @return El entrenador ganador si alguno se queda sin Pokémon, null si la batalla continúa
     */
    private Entrenador verificarCondicionesVictoria() {
        if (!entrenador1.puedeCombatir()) {
            System.out.println(entrenador1.getNombre() + " se ha quedado sin Pokémon!");
            batallaTeminada = true;
            return entrenador2;
        }
        
        if (!entrenador2.puedeCombatir()) {
            System.out.println(entrenador2.getNombre() + " se ha quedado sin Pokémon!");
            batallaTeminada = true;
            return entrenador1;
        }
        return null;
    }

    /**
     * Procesa los efectos de estado al final del turno
     * @param entrenadorActual El entrenador actual
     * @param pokemonActual El Pokémon actual
     * @return El entrenador ganador si el Pokémon se debilita por efectos de estado, null si continúa
     */
    private Entrenador procesarEfectosEstadoFinTurno(Entrenador entrenadorActual, Pokemon pokemonActual) {
        String efectoEstado = pokemonActual.aplicarEfectosEstado();
        if (!efectoEstado.isEmpty()) {
            System.out.println(efectoEstado);
        }

        if (pokemonActual.estaDebilitado()) {
            System.out.println(pokemonActual.getName() + " se ha debilitado debido a su estado!");
            try {
                Pokemon pokemonActivo = turnoEntrenador1 ? pokemon1 : pokemon2;
                if (pokemonActual == pokemonActivo) {
                    cambiarPokemonDebilitado(entrenadorActual);
                }
            } catch (POOBkemonException e) {
                System.out.println("Error al cambiar Pokémon debilitado: " + e.getMessage());
                if (!entrenadorActual.puedeCombatir()) {
                    System.out.println(entrenadorActual.getNombre() + " se ha quedado sin Pokémon!");
                    batallaTeminada = true;
                    Entrenador entrenadorOponente = turnoEntrenador1 ? entrenador2 : entrenador1;
                    return entrenadorOponente;
                }
            }
        }
        return null;
    }

    /**
     * Maneja errores inesperados durante un turno
     * @param e La excepción ocurrida
     */
    private void manejarErrorTurno(Exception e) {
        System.out.println("Error inesperado durante la batalla: " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Determina el ganador final en caso de empate o situaciones especiales
     * @return El entrenador ganador o null en caso de empate
     */
    private Entrenador determinarGanadorFinal() {
        if (entrenador1.puedeCombatir() && !entrenador2.puedeCombatir()) {
            return entrenador1;
        } else if (!entrenador1.puedeCombatir() && entrenador2.puedeCombatir()) {
            return entrenador2;
        } else {
            return null;
        }
    }
    
    /**
     * Realiza un ataque entre dos Pokémon.
     * @param entrenador El entrenador que ordena el ataque.
     * @param atacante El Pokémon que realiza el ataque.
     * @param defensor El Pokémon que recibe el ataque.
     * @throws POOBkemonException Si ocurre un error al elegir el movimiento.
     */
    private void realizarAtaque(Entrenador entrenador, Pokemon atacante, Pokemon defensor) throws POOBkemonException {
        if (atacante.getMovimientos() == null || atacante.getMovimientos().isEmpty()) {
            throw new POOBkemonException("¡" + atacante.getName() + " no tiene movimientos disponibles!");
        }
        
        Movimiento movimiento = seleccionarMovimiento(entrenador, atacante);
        
        System.out.println(atacante.getName() + " usa " + movimiento.getNombre() + "!");

        String resultadoIntento = atacante.procesarIntentarAtacar();
        if (resultadoIntento != null) {
            System.out.println(resultadoIntento);
            movimiento.usarMovimiento();
            return;
        }
        
        ejecutarAtaque(movimiento, atacante, defensor);
    }

    /**
     * Selecciona el movimiento a usar, considerando la disponibilidad de PP
     * @param entrenador El entrenador que elige
     * @param atacante El Pokémon atacante
     * @return El movimiento seleccionado
     * @throws POOBkemonException Si hay error en la selección
     */
    private Movimiento seleccionarMovimiento(Entrenador entrenador, Pokemon atacante) throws POOBkemonException {
        boolean tienePP = false;
        for (Movimiento mov : atacante.getMovimientos()) {
            if (mov.tienePPDisponibles()) {
                tienePP = true;
                break;
            }
        }
        if (!tienePP) {
            System.out.println("¡" + atacante.getName() + " no tiene PP disponibles! Usará Forcejeo.");
            return MOVIMIENTO_FORCEJEO;
        }
        Movimiento movimiento = entrenador.elegirMovimiento(atacante, scanner);
        if (movimiento == null) {
            throw new POOBkemonException("¡El movimiento seleccionado no está disponible!");
        }
        if (!movimiento.tienePPDisponibles()) {
            throw new POOBkemonException("¡El movimiento " + movimiento.getNombre() + " no tiene PP disponibles!");
        }
        return movimiento;
    }

    /**
     * Ejecuta el ataque seleccionado
     * @param movimiento El movimiento a ejecutar
     * @param atacante El Pokémon atacante
     * @param defensor El Pokémon defensor
     */
    private void ejecutarAtaque(Movimiento movimiento, Pokemon atacante, Pokemon defensor) {
        boolean acierta = comprobarPrecision(movimiento, atacante);
        
        if (acierta) {
            String resultadoAtaque = movimiento.aplicarEfecto(atacante, defensor);
            movimiento.usarMovimiento();
            System.out.println(resultadoAtaque);
            procesarConsecuenciasAtaque(atacante, defensor);
        } else {
            movimiento.usarMovimiento();
            System.out.println("¡El ataque ha fallado!");
        }
    }

    /**
     * Procesa las consecuencias del ataque (Pokémon debilitados)
     * @param atacante El Pokémon atacante
     * @param defensor El Pokémon defensor
     */
    private void procesarConsecuenciasAtaque(Pokemon atacante, Pokemon defensor) {
        if (defensor.estaDebilitado()) {
            System.out.println(defensor.getName() + " se ha debilitado!");
            Entrenador entrenadorDefensor = (defensor == pokemon1) ? entrenador1 : entrenador2;
            try {
                cambiarPokemonDebilitado(entrenadorDefensor);
            } catch (POOBkemonException e) {
                System.out.println("Error al cambiar Pokémon debilitado: " + e.getMessage());
            }
        }
        if (atacante.estaDebilitado() && 
            ((turnoEntrenador1 && atacante == pokemon1) || (!turnoEntrenador1 && atacante == pokemon2))) {
            System.out.println(atacante.getName() + " se ha debilitado debido al retroceso!");
            Entrenador entrenadorAtacante = (atacante == pokemon1) ? entrenador1 : entrenador2;
            try {
                cambiarPokemonDebilitado(entrenadorAtacante);
            } catch (POOBkemonException e) {
                System.out.println("Error al cambiar Pokémon debilitado: " + e.getMessage());
            }
        }
    }
    
    /**
     * Comprueba si un movimiento acierta basado en su precisión.
     * @param movimiento El movimiento a comprobar.
     * @param usuario El Pokémon que utiliza el movimiento.
     * @return true si el movimiento acierta, false si falla.
     */
    private boolean comprobarPrecision(Movimiento movimiento, Pokemon usuario) {
        int precision = movimiento.getPrecision();
        double multiplicadorPrecision = usuario.getMultiplicadorPrecision();
        if (precision == 0) return true;
        double probabilidadAcierto = precision * multiplicadorPrecision / 100.0;
        double resultado = random.nextDouble();
        return resultado <= probabilidadAcierto;
    }
    
    /**
     * Permite a un entrenador cambiar su Pokémon activo.
     * @param entrenador El entrenador que cambia de Pokémon.
     * @throws POOBkemonException Si ocurre un error al elegir el Pokémon.
     */
    private void cambiarPokemon(Entrenador entrenador) throws POOBkemonException {
        if (!tieneMasDeUnPokemonDisponible(entrenador)) {
            throw new POOBkemonException("No tienes más Pokémon disponibles para cambiar.");
        }
        
        Pokemon nuevoPokemon = entrenador.elegirPokemon();

        if (nuevoPokemon.estaDebilitado()) {
            throw new POOBkemonException("No puedes elegir un Pokémon debilitado.");
        }

        if (entrenador == entrenador1) {
            pokemon1 = nuevoPokemon;
            System.out.println(entrenador1.getNombre() + " cambia a " + pokemon1.getName() + "!");
        } else {
            pokemon2 = nuevoPokemon;
            System.out.println(entrenador2.getNombre() + " cambia a " + pokemon2.getName() + "!");
        }
    }
    
    /**
     * Verifica si un entrenador tiene más de un Pokémon disponible (no debilitado).
     * @param entrenador El entrenador a verificar.
     * @return true si tiene más de un Pokémon disponible, false en caso contrario.
     */
    private boolean tieneMasDeUnPokemonDisponible(Entrenador entrenador) {
        int disponibles = 0;
        for (Pokemon pokemon : entrenador.getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                disponibles++;
                if (disponibles > 1) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Fuerza a un entrenador a cambiar su Pokémon debilitado.
     * @param entrenador El entrenador que debe cambiar de Pokémon.
     * @throws POOBkemonException Si ocurre un error al elegir el Pokémon.
     */
    private void cambiarPokemonDebilitado(Entrenador entrenador) throws POOBkemonException {
        if (!entrenador.puedeCombatir()) {
            // El entrenador no tiene más Pokémon disponibles
            throw new POOBkemonException("No hay más Pokémon disponibles.");
        }
        
        Pokemon nuevoPokemon = entrenador.elegirPokemon();

        if (nuevoPokemon.estaDebilitado()) {
            throw new POOBkemonException("No puedes elegir un Pokémon debilitado.");
        }

        if (entrenador == entrenador1) {
            pokemon1 = nuevoPokemon;
            System.out.println(entrenador1.getNombre() + " envía a " + pokemon1.getName() + "!");
        } else {
            pokemon2 = nuevoPokemon;
            System.out.println(entrenador2.getNombre() + " envía a " + pokemon2.getName() + "!");
        }
    }
    
    /**
     * Permite a un entrenador usar un objeto.
     * @param entrenador El entrenador que usa el objeto.
     * @throws POOBkemonException Si ocurre un error al elegir o usar el objeto.
     */
    private void usarObjeto(Entrenador entrenador) throws POOBkemonException {
        if (entrenador.getItems().isEmpty()) {
            throw new POOBkemonException(entrenador.getNombre() + " no tiene objetos para usar.");
        }
        
        Item item = entrenador.elegirItem();
        Pokemon objetivo = entrenador.elegirPokemonParaItem();
        
        System.out.println(entrenador.getNombre() + " usa " + item.getNombre() + " en " + objetivo.getName() + "!");
        
        try {
            entrenador.usarItem(item, objetivo);
            System.out.println("¡" + item.getNombre() + " aplicado con éxito a " + objetivo.getName() + "!");
        } catch (POOBkemonException e) {
            System.out.println("Error al usar el objeto: " + e.getMessage());
            throw e;
        }

        actualizarPokemonActivosSiEsNecesario(entrenador);
    }

    /**
     * Actualiza los Pokémon activos si el actual está debilitado
     * @param entrenador El entrenador cuyo Pokémon puede necesitar actualización
     */
    private void actualizarPokemonActivosSiEsNecesario(Entrenador entrenador) {
        if (entrenador == entrenador1 && pokemon1.estaDebilitado()) {
            pokemon1 = entrenador1.getPrimerPokemonDisponible();
            if (pokemon1 != null) {
                System.out.println(entrenador1.getNombre() + " debe utilizar a " + pokemon1.getName() + "!");
            }
        } else if (entrenador == entrenador2 && pokemon2.estaDebilitado()) {
            pokemon2 = entrenador2.getPrimerPokemonDisponible();
            if (pokemon2 != null) {
                System.out.println(entrenador2.getNombre() + " debe utilizar a " + pokemon2.getName() + "!");
            }
        }
    }
    
    /**
     * Muestra el estado actual de los Pokémon en batalla.
     */
    private void mostrarEstadoPokemon() {
        System.out.println("\n=== Estado actual ===");
        System.out.println(entrenador1.getNombre() + ": " + pokemon1.getName() + " - PS: " + 
                          pokemon1.getPs() + "/" + pokemon1.getMaxPs() + 
                          (pokemon1.tieneEstado() ? " [" + pokemon1.getStatus() + "]" : ""));
        
        System.out.println(entrenador2.getNombre() + ": " + pokemon2.getName() + " - PS: " + 
                          pokemon2.getPs() + "/" + pokemon2.getMaxPs() + 
                          (pokemon2.tieneEstado() ? " [" + pokemon2.getStatus() + "]" : ""));
    }
    
    /**
     * Cambia el turno al otro entrenador.
     */
    private void cambiarTurno() {
        turnoEntrenador1 = !turnoEntrenador1;
    }
    
    /**
     * Verifica si la batalla ha terminado.
     * @return true si la batalla ha terminado, false en caso contrario.
     */
    public boolean estaBatallaTerminada() {
        return batallaTeminada;
    }
    
    /**
     * Obtiene el entrenador 1.
     * @return El entrenador 1.
     */
    public Entrenador getEntrenador1() {
        return entrenador1;
    }
    
    /**
     * Obtiene el entrenador 2.
     * @return El entrenador 2.
     */
    public Entrenador getEntrenador2() {
        return entrenador2;
    }
    
    /**
     * Obtiene el Pokémon activo del entrenador 1.
     * @return El Pokémon activo del entrenador 1.
     */
    public Pokemon getPokemon1() {
        return pokemon1;
    }
    
    /**
     * Obtiene el Pokémon activo del entrenador 2.
     * @return El Pokémon activo del entrenador 2.
     */
    public Pokemon getPokemon2() {
        return pokemon2;
    }
}