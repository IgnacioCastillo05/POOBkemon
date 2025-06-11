package domain;

import java.util.*;

/**
 * Entrenador máquina con estrategia ofensiva.
 * Prioriza ataques de alto poder y movimientos que mejoran estadísticas ofensivas.
 */
public class AttackingTrainer extends EntrenadorMaquina {
    
    public AttackingTrainer(String nombre) {
        super(nombre, "attacking");
    }
    
    @Override
    protected Pokemon elegirMejorPokemon() {
        if (!tieneMasDeUnPokemonDisponible()) {
            return getPrimerPokemonDisponible();
        }

        Pokemon mejorPokemon = null;
        int mayorEstadicticaAtaque = 0;
        
        for (Pokemon pokemon : getPokemones()) {
            if (!pokemon.estaDebilitado()) {
                int estadisticaAtaque = Math.max(pokemon.getAttack(), pokemon.getSpecialAttack());
                
                if (mejorPokemon == null || estadisticaAtaque > mayorEstadicticaAtaque) {
                    mejorPokemon = pokemon;
                    mayorEstadicticaAtaque = estadisticaAtaque;
                }
            }
        }
        
        return mejorPokemon;
    }
    
    @Override
    protected Movimiento elegirMejorMovimiento(Pokemon pokemon) {
        List<Movimiento> movimientos = pokemon.getMovimientos();
        
        if (movimientos == null || movimientos.isEmpty()) {
            return null;
        }
        List<Movimiento> movimientosDisponibles = filtrarMovimientosConPP(movimientos);
        if (movimientosDisponibles.isEmpty()) {
            return null;
        }
        
        return elegirMejorMovimiento(pokemon, movimientosDisponibles);
    }
    
    @Override
    protected Movimiento elegirMejorMovimiento(Pokemon pokemon, List<Movimiento> movimientosDisponibles) {
        for (Movimiento movimiento : movimientosDisponibles) {
            String nombreMovimiento = movimiento.getNombre();
            if (movimiento instanceof MovimientoEstado && 
                (nombreMovimiento.equals("Afilagarras") || 
                 nombreMovimiento.equals("Danza Dragon") ||
                 nombreMovimiento.equals("Tambor") ||
                 nombreMovimiento.equals("Corpulencia"))) {

                if (pokemon.getPs() > pokemon.getMaxPs() * 0.7) {
                    return movimiento;
                }
            }
        }

        Movimiento mejorMovimiento = null;
        double mejorPuntaje = 0;
        
        for (Movimiento movimiento : movimientosDisponibles) {
            if (movimiento instanceof MovimientoFisico || movimiento instanceof MovimientoEspecial) {
                double puntaje = calcularPuntajeMovimiento(movimiento, pokemon);
                
                if (mejorMovimiento == null || puntaje > mejorPuntaje) {
                    mejorMovimiento = movimiento;
                    mejorPuntaje = puntaje;
                }
            }
        }
        if (mejorMovimiento == null && !movimientosDisponibles.isEmpty()) {
            return movimientosDisponibles.get(random.nextInt(movimientosDisponibles.size()));
        }
        
        return mejorMovimiento;
    }
    
    /**
     * Calcula un puntaje para un movimiento ofensivo basado en su poder y compatibilidad con el Pokémon
     */
    private double calcularPuntajeMovimiento(Movimiento movimiento, Pokemon pokemon) {
        int poder = movimiento.getPoder();
        double puntaje = poder;

        if (movimiento.getTipo().equals(pokemon.getTipo1()) || 
            (pokemon.getTipo2() != null && movimiento.getTipo().equals(pokemon.getTipo2()))) {
            puntaje *= 1.5;
        }

        if (movimiento instanceof MovimientoFisico && pokemon.getAttack() > pokemon.getSpecialAttack()) {
            puntaje *= 1.2;
        } else if (movimiento instanceof MovimientoEspecial && pokemon.getSpecialAttack() > pokemon.getAttack()) {
            puntaje *= 1.2;
        }

        int precision = movimiento.getPrecision();
        if (precision > 0) {
            if (poder >= 100) {
                puntaje *= Math.max(0.7, precision / 100.0);
            } else if (poder >= 80) {
                puntaje *= Math.max(0.8, precision / 100.0);
            } else {
                puntaje *= (precision / 100.0);
            }
        }

        String nombreMovimiento = movimiento.getNombre();
        if (nombreMovimiento.equals("Cabezazo") || nombreMovimiento.equals("Golpe Salto") ||
            nombreMovimiento.equals("Doble Filo") || nombreMovimiento.equals("Ataque Furia")) {
            puntaje *= 1.1;
        }
        
        return puntaje;
    }
    
    @Override
    public int elegirAccion(Entrenador oponente, Scanner scanner) throws POOBkemonException {
        Pokemon miPokemon = getPrimerPokemonDisponible();
        Pokemon pokemonOponente = oponente.getPrimerPokemonDisponible();

        if (miPokemon.getPs() < miPokemon.getMaxPs() * 0.3 && !getItems().isEmpty()) {
            for (Item item : getItems()) {
                if (item instanceof Potion) {
                    return 3;
                }
            }
        }
        if (esDesventajaDeTipo(miPokemon, pokemonOponente) && tieneMasDeUnPokemonDisponible()) {
            if (random.nextDouble() < 0.3) {
                return 2;
            }
        }
        return 1;
    }
    
    /**
     * Determina si un Pokémon está en desventaja de tipo contra otro
     */
    private boolean esDesventajaDeTipo(Pokemon miPokemon, Pokemon pokemonOponente) {
        if (miPokemon == null || pokemonOponente == null) {
            return false;
        }
        
        Tipo tipoOponente1 = pokemonOponente.getTipo1();
        Tipo tipoOponente2 = pokemonOponente.getTipo2();

        boolean desventaja = false;
        
        if (tipoOponente1 != null) {
            double efectividad = tipoOponente1.calcularEfectividadContraPokemon(miPokemon);
            if (efectividad > 1.0) {
                desventaja = true;
            }
        }
        
        if (tipoOponente2 != null) {
            double efectividad = tipoOponente2.calcularEfectividadContraPokemon(miPokemon);
            if (efectividad > 1.0) {
                desventaja = true;
            }
        }
        
        return desventaja;
    }
    
    @Override
    protected boolean deberiaUsarItem(Pokemon pokemon) {
        if (pokemon.getPs() < pokemon.getMaxPs() * 0.4) {
            return true;
        }

        if (pokemon.tieneEstado()) {
            String estado = pokemon.getStatus();
            if (estado.equals("quemado") || estado.equals("paralizado")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Item elegirItem() throws POOBkemonException {
        List<Item> items = getItems();
        
        if (items.isEmpty()) {
            throw new POOBkemonException("No hay items disponibles.");
        }
        boolean tienePokemonDebilitado = false;
        for (Pokemon pokemon : getPokemones()) {
            if (pokemon.estaDebilitado()) {
                tienePokemonDebilitado = true;
                break;
            }
        }

        if (tienePokemonDebilitado) {
            for (Item item : items) {
                if (item instanceof Revive) {
                    return item;
                }
            }
        }

        for (Item item : items) {
            if (item instanceof HyperPotion) {
                return item;
            }
        }
        
        for (Item item : items) {
            if (item instanceof SuperPotion) {
                return item;
            }
        }
        
        for (Item item : items) {
            if (item instanceof NormalPotion) {
                return item;
            }
        }

        return items.get(0);
    }
}