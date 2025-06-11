import domain.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokemonSerializer {
    
    public static void main(String[] args) {


        ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
        Map<String, Movimiento> movimientosMap = cargarMovimientos("movimientos.bin");

        // Obtener referencias a todos los movimientos
        Movimiento lanzallamas = movimientosMap.get("Lanzallamas");
        Movimiento fuegofatuo = movimientosMap.get("Fuegofatuo");
        Movimiento ruedaFuego = movimientosMap.get("Rueda Fuego");
        Movimiento pajaroOsado = movimientosMap.get("Pájaro Osado");
        Movimiento hiperrayo = movimientosMap.get("Hiperrayo");
        Movimiento pulimento = movimientosMap.get("Pulimento");
        Movimiento golpeCuerpo = movimientosMap.get("Golpe Cuerpo");
        Movimiento hidrobomba = movimientosMap.get("Hidrobomba");
        Movimiento buceo = movimientosMap.get("Buceo");
        Movimiento refugio = movimientosMap.get("Refugio");
        Movimiento puñoHielo = movimientosMap.get("Puño Hielo");
        Movimiento picadura = movimientosMap.get("Picadura");
        Movimiento disparoDemora = movimientosMap.get("Disparo Demora");
        Movimiento garraMetal = movimientosMap.get("Garra Metal");
        Movimiento focoResplandor = movimientosMap.get("Foco Resplandor");
        Movimiento estoicismo = movimientosMap.get("Estoicisimo");
        Movimiento aBocajarro = movimientosMap.get("A Bocajarro");
        Movimiento triturar = movimientosMap.get("Triturar");
        Movimiento pulsoUmbrio = movimientosMap.get("Pulso Umbrio");
        Movimiento afilaGarras = movimientosMap.get("Afilagarras");
        Movimiento terremoto = movimientosMap.get("Terremoto");
        Movimiento danzaDragon = movimientosMap.get("Danza Dragon");
        Movimiento garraDragon = movimientosMap.get("Garra Dragon");
        Movimiento cometaDraco = movimientosMap.get("Cometa Draco");
        Movimiento respiro = movimientosMap.get("Respiro");
        Movimiento rayo = movimientosMap.get("Rayo");
        Movimiento colmilloTrueno = movimientosMap.get("Colmillo Trueno");
        Movimiento ondaTrueno = movimientosMap.get("Onda Trueno");
        Movimiento encanto = movimientosMap.get("Encanto");
        Movimiento corpulencia = movimientosMap.get("Corpulencia");
        Movimiento tambor = movimientosMap.get("Tambor");
        Movimiento carantoña = movimientosMap.get("Carantoña");
        Movimiento fuerzaLunar = movimientosMap.get("Fuerza Lunar");
        Movimiento esferaAural = movimientosMap.get("Esfera Aural");
        Movimiento ataqueArena = movimientosMap.get("Ataque Arena");
        Movimiento puyaNociva = movimientosMap.get("Puya Nociva");
        Movimiento bombaLodo = movimientosMap.get("Bomba Lodo");
        Movimiento huracan = movimientosMap.get("Huracán");
        Movimiento bolaSombra = movimientosMap.get("Bola Sombra");
        Movimiento garraUmbria = movimientosMap.get("Garra Umbría");
        Movimiento rayoConfuso = movimientosMap.get("Rayo Confuso");
        Movimiento rayoSolar = movimientosMap.get("Rayo Solar");
        Movimiento hojaAguda = movimientosMap.get("Hoja Aguda");
        Movimiento paralizador = movimientosMap.get("Paralizador");
        Movimiento toxico = movimientosMap.get("Tóxico");
        Movimiento disparoLodo = movimientosMap.get("Disparo Lodo");
        Movimiento rayoHielo = movimientosMap.get("Rayo Hielo");
        Movimiento veloAurora = movimientosMap.get("Velo Aurora");
        Movimiento psiquico = movimientosMap.get("Psíquico");
        Movimiento cabezazoZen = movimientosMap.get("Cabezazo Zen");
        Movimiento hipnosis = movimientosMap.get("Hipnosis");
        Movimiento rocaAfilada = movimientosMap.get("Roca Afilada");
        Movimiento poderPasado = movimientosMap.get("Poder Pasado");
        Movimiento ecoMetalico = movimientosMap.get("Eco Metálico");

        // Crear los tipos
        Tipo tipoBicho = new Tipo("Bug");
        Tipo tipoSiniestro = new Tipo("Dark");
        Tipo tipoDragon = new Tipo("Dragon");
        Tipo tipoElectrico = new Tipo("Electric");
        Tipo tipoHada = new Tipo("Fairy");
        Tipo tipoLucha = new Tipo("Fighting");
        Tipo tipoFuego = new Tipo("Fire");
        Tipo tipoVolador = new Tipo("Flying");
        Tipo tipoFantasma = new Tipo("Ghost");
        Tipo tipoPlanta = new Tipo("Grass");
        Tipo tipoTierra = new Tipo("Ground");
        Tipo tipoHielo = new Tipo("Ice");
        Tipo tipoNormal = new Tipo("Normal");
        Tipo tipoVeneno = new Tipo("Poison");
        Tipo tipoPsiquico = new Tipo("Psychic");
        Tipo tipoRoca = new Tipo("Rock");
        Tipo tipoAcero = new Tipo("Steel");
        Tipo tipoAgua = new Tipo("Water");

        // Crear todos los Pokémon con sus movimientos
        List<Movimiento> movimientosCharizard = new ArrayList<>();
        movimientosCharizard.add(lanzallamas);
        movimientosCharizard.add(fuegofatuo);
        movimientosCharizard.add(ruedaFuego);
        movimientosCharizard.add(pajaroOsado);
        Pokemon charizard = new Pokemon(360, 360, 293, 280, 328, 348, 295, "Charizard", tipoFuego, tipoVolador, movimientosCharizard);
        pokemons.add(charizard);

        List<Movimiento> movimientosArcanine = new ArrayList<>();
        movimientosArcanine.add(lanzallamas);
        movimientosArcanine.add(ruedaFuego);
        movimientosArcanine.add(hiperrayo);
        movimientosArcanine.add(pulimento);
        Pokemon arcanine = new Pokemon(384, 384, 350, 284, 317, 328, 284, "Arcanine", tipoFuego, null, movimientosArcanine);
        pokemons.add(arcanine);

        List<Movimiento> movimientosBlastoise = new ArrayList<>();
        movimientosBlastoise.add(golpeCuerpo);
        movimientosBlastoise.add(hidrobomba);
        movimientosBlastoise.add(buceo);
        movimientosBlastoise.add(refugio);
        Pokemon blastoise = new Pokemon(364, 364, 291, 328, 280, 295, 339, "Blastoise", tipoAgua, null, movimientosBlastoise);
        pokemons.add(blastoise);

        List<Movimiento> movimientosFeraligatr = new ArrayList<>();
        movimientosFeraligatr.add(puñoHielo);
        movimientosFeraligatr.add(hiperrayo);
        movimientosFeraligatr.add(hidrobomba);
        movimientosFeraligatr.add(refugio);
        Pokemon feraligatr = new Pokemon(374, 374, 339, 328, 280, 282, 291, "Feraligatr", tipoAgua, null, movimientosFeraligatr);
        pokemons.add(feraligatr);

        List<Movimiento> movimientosForretress = new ArrayList<>();
        movimientosForretress.add(picadura);
        movimientosForretress.add(disparoDemora);
        movimientosForretress.add(garraMetal);
        movimientosForretress.add(focoResplandor);
        Pokemon forretress = new Pokemon(354, 354, 306, 416, 196, 240, 240, "Forretress", tipoBicho, tipoAcero, movimientosForretress);
        pokemons.add(forretress);

        List<Movimiento> movimientosHeracross = new ArrayList<>();
        movimientosHeracross.add(estoicismo);
        movimientosHeracross.add(picadura);
        movimientosHeracross.add(disparoDemora);
        movimientosHeracross.add(aBocajarro);
        Pokemon heracross = new Pokemon(364, 364, 383, 273, 295, 196, 317, "Heracross", tipoBicho, tipoLucha, movimientosHeracross);
        pokemons.add(heracross);

        List<Movimiento> movimientosAbsol = new ArrayList<>();
        movimientosAbsol.add(triturar);
        movimientosAbsol.add(pulsoUmbrio);
        movimientosAbsol.add(afilaGarras);
        movimientosAbsol.add(hiperrayo);
        Pokemon absol = new Pokemon(334, 334, 394, 240, 273, 273, 240, "Absol", tipoSiniestro, null, movimientosAbsol);
        pokemons.add(absol);

        List<Movimiento> movimientosUmbreon = new ArrayList<>();
        movimientosUmbreon.add(pulsoUmbrio);
        movimientosUmbreon.add(afilaGarras);
        movimientosUmbreon.add(terremoto);
        movimientosUmbreon.add(golpeCuerpo);
        Pokemon umbreon = new Pokemon(363, 363, 251, 350, 251, 240, 394, "Umbreon", tipoSiniestro, null, movimientosUmbreon);
        pokemons.add(umbreon);

        List<Movimiento> movimientosDragonite = new ArrayList<>();
        movimientosDragonite.add(danzaDragon);
        movimientosDragonite.add(garraDragon);
        movimientosDragonite.add(cometaDraco);
        movimientosDragonite.add(pajaroOsado);
        Pokemon dragonite = new Pokemon(386, 386, 403, 317, 284, 328, 328, "Dragonite", tipoDragon, tipoVolador, movimientosDragonite);
        pokemons.add(dragonite);

        List<Movimiento> movimientosSalamence = new ArrayList<>();
        movimientosSalamence.add(danzaDragon);
        movimientosSalamence.add(garraDragon);
        movimientosSalamence.add(cometaDraco);
        movimientosSalamence.add(respiro);
        Pokemon salamence = new Pokemon(394, 394, 405, 284, 328, 350, 284, "Salamence", tipoDragon, tipoVolador, movimientosSalamence);
        pokemons.add(salamence);

        List<Movimiento> movimientosAmpharos = new ArrayList<>();
        movimientosAmpharos.add(rayo);
        movimientosAmpharos.add(colmilloTrueno);
        movimientosAmpharos.add(ondaTrueno);
        movimientosAmpharos.add(encanto);
        Pokemon ampharos = new Pokemon(384, 394, 273, 295, 229, 361, 306, "Ampharos", tipoElectrico, null, movimientosAmpharos);
        pokemons.add(ampharos);

        List<Movimiento> movimientosRaichu = new ArrayList<>();
        movimientosRaichu.add(rayo);
        movimientosRaichu.add(ondaTrueno);
        movimientosRaichu.add(corpulencia);
        movimientosRaichu.add(tambor);
        Pokemon raichu = new Pokemon(324, 324, 306, 229, 350, 306, 284, "Raichu", tipoElectrico, null, movimientosRaichu);
        pokemons.add(raichu);

        List<Movimiento> movimientosClefable = new ArrayList<>();
        movimientosClefable.add(carantoña);
        movimientosClefable.add(fuerzaLunar);
        movimientosClefable.add(encanto);
        movimientosClefable.add(hiperrayo);
        Pokemon clefable = new Pokemon(394, 394, 262, 269, 240, 317, 306, "Clefable", tipoHada, null, movimientosClefable);
        pokemons.add(clefable);

        List<Movimiento> movimientosTogetic = new ArrayList<>();
        movimientosTogetic.add(carantoña);
        movimientosTogetic.add(fuerzaLunar);
        movimientosTogetic.add(respiro);
        movimientosTogetic.add(pajaroOsado);
        Pokemon togetic = new Pokemon(314, 314, 196, 295, 196, 284, 339, "Togetic", tipoHada, tipoVolador, movimientosTogetic);
        pokemons.add(togetic);

        List<Movimiento> movimientosMachamp = new ArrayList<>();
        movimientosMachamp.add(esferaAural);
        movimientosMachamp.add(aBocajarro);
        movimientosMachamp.add(corpulencia);
        movimientosMachamp.add(ataqueArena);
        Pokemon machamp = new Pokemon(384, 384, 394, 284, 229, 251, 295, "Machamp", tipoLucha, null, movimientosMachamp);
        pokemons.add(machamp);

        List<Movimiento> movimientosPrimeape = new ArrayList<>();
        movimientosPrimeape.add(esferaAural);
        movimientosPrimeape.add(aBocajarro);
        movimientosPrimeape.add(corpulencia);
        movimientosPrimeape.add(golpeCuerpo);
        Pokemon primeape = new Pokemon(334, 334, 339, 240, 317, 240, 262, "Primeape", tipoLucha, null, movimientosPrimeape);
        pokemons.add(primeape);

        List<Movimiento> movimientosCrobat = new ArrayList<>();
        movimientosCrobat.add(respiro);
        movimientosCrobat.add(pajaroOsado);
        movimientosCrobat.add(puyaNociva);
        movimientosCrobat.add(bombaLodo);
        Pokemon crobat = new Pokemon(374, 374, 306, 284, 394, 262, 284, "Crobat", tipoVeneno, tipoVolador, movimientosCrobat);
        pokemons.add(crobat);

        List<Movimiento> movimientosGyarados = new ArrayList<>();
        movimientosGyarados.add(pajaroOsado);
        movimientosGyarados.add(huracan);
        movimientosGyarados.add(hidrobomba);
        movimientosGyarados.add(refugio);
        Pokemon gyarados = new Pokemon(394, 394, 383, 282, 287, 240, 328, "Gyarados", tipoAgua, tipoVolador, movimientosGyarados);
        pokemons.add(gyarados);

        List<Movimiento> movimientosBanette = new ArrayList<>();
        movimientosBanette.add(bolaSombra);
        movimientosBanette.add(garraUmbria);
        movimientosBanette.add(rayoConfuso);
        movimientosBanette.add(terremoto);
        Pokemon banette = new Pokemon(332, 332, 361, 251, 251, 291, 247, "Banette", tipoFantasma, null, movimientosBanette);
        pokemons.add(banette);

        List<Movimiento> movimientosGengar = new ArrayList<>();
        movimientosGengar.add(bolaSombra);
        movimientosGengar.add(rayoConfuso);
        movimientosGengar.add(puyaNociva);
        movimientosGengar.add(bombaLodo);
        Pokemon gengar = new Pokemon(324, 324, 251, 240, 350, 394, 273, "Gengar", tipoFantasma, tipoVeneno, movimientosGengar);
        pokemons.add(gengar);

        List<Movimiento> movimientosVenusaur = new ArrayList<>();
        movimientosVenusaur.add(rayoSolar);
        movimientosVenusaur.add(hojaAguda);
        movimientosVenusaur.add(paralizador);
        movimientosVenusaur.add(toxico);
        Pokemon venusaur = new Pokemon(364, 364, 289, 291, 284, 328, 328, "Venusaur", tipoPlanta, tipoVeneno, movimientosVenusaur);
        pokemons.add(venusaur);

        List<Movimiento> movimientosSceptile = new ArrayList<>();
        movimientosSceptile.add(aBocajarro);
        movimientosSceptile.add(hojaAguda);
        movimientosSceptile.add(paralizador);
        movimientosSceptile.add(terremoto);
        Pokemon sceptile = new Pokemon(344, 344, 295, 251, 372, 339, 295, "Sceptile", tipoPlanta, null, movimientosSceptile);
        pokemons.add(sceptile);

        List<Movimiento> movimientosDonphan = new ArrayList<>();
        movimientosDonphan.add(terremoto);
        movimientosDonphan.add(disparoLodo);
        movimientosDonphan.add(ataqueArena);
        movimientosDonphan.add(hiperrayo);
        Pokemon donphan = new Pokemon(384, 384, 372, 372, 218, 240, 240, "Donphan", tipoTierra, null, movimientosDonphan);
        pokemons.add(donphan);

        List<Movimiento> movimientosRhydon = new ArrayList<>();
        movimientosRhydon.add(terremoto);
        movimientosRhydon.add(ataqueArena);
        movimientosRhydon.add(rocaAfilada);
        movimientosRhydon.add(poderPasado);
        Pokemon rhydon = new Pokemon(414, 414, 394, 372, 196, 207, 207, "Rhydon", tipoTierra, tipoRoca, movimientosRhydon);
        pokemons.add(rhydon);

        List<Movimiento> movimientosDelibird = new ArrayList<>();
        movimientosDelibird.add(respiro);
        movimientosDelibird.add(huracan);
        movimientosDelibird.add(puñoHielo);
        movimientosDelibird.add(rayoHielo);
        Pokemon delibird = new Pokemon(294, 294, 229, 207, 273, 251, 207, "Delibird", tipoHielo, tipoVolador, movimientosDelibird);
        pokemons.add(delibird);

        List<Movimiento> movimientosPiloswine = new ArrayList<>();
        movimientosPiloswine.add(terremoto);
        movimientosPiloswine.add(disparoLodo);
        movimientosPiloswine.add(rayoHielo);
        movimientosPiloswine.add(veloAurora);
        Pokemon piloswine = new Pokemon(404, 404, 328, 284, 218, 240, 240, "Piloswine", tipoHielo, tipoTierra, movimientosPiloswine);
        pokemons.add(piloswine);

        List<Movimiento> movimientosPorygon2 = new ArrayList<>();
        movimientosPorygon2.add(ondaTrueno);
        movimientosPorygon2.add(golpeCuerpo);
        movimientosPorygon2.add(hiperrayo);
        movimientosPorygon2.add(tambor);
        Pokemon porygon2 = new Pokemon(374, 374, 284, 306, 240, 339, 317, "Porygon2", tipoNormal, null, movimientosPorygon2);
        pokemons.add(porygon2);

        List<Movimiento> movimientosSnorlax = new ArrayList<>();
        movimientosSnorlax.add(terremoto);
        movimientosSnorlax.add(ataqueArena);
        movimientosSnorlax.add(golpeCuerpo);
        movimientosSnorlax.add(hiperrayo);
        Pokemon snorlax = new Pokemon(524, 524, 350, 251, 174, 251, 350, "Snorlax", tipoNormal, null, movimientosSnorlax);
        pokemons.add(snorlax);

        List<Movimiento> movimientosMuk = new ArrayList<>();
        movimientosMuk.add(tambor);
        movimientosMuk.add(puyaNociva);
        movimientosMuk.add(bombaLodo);
        movimientosMuk.add(toxico);
        Pokemon muk = new Pokemon(414, 414, 339, 273, 218, 251, 328, "Muk", tipoVeneno, null, movimientosMuk);
        pokemons.add(muk);

        List<Movimiento> movimientosWeezing = new ArrayList<>();
        movimientosWeezing.add(hiperrayo);
        movimientosWeezing.add(puyaNociva);
        movimientosWeezing.add(bombaLodo);
        movimientosWeezing.add(toxico);
        Pokemon weezing = new Pokemon(334, 334, 306, 372, 240, 295, 262, "Weezing", tipoVeneno, null, movimientosWeezing);
        pokemons.add(weezing);

        List<Movimiento> movimientosGardevoir = new ArrayList<>();
        movimientosGardevoir.add(carantoña);
        movimientosGardevoir.add(fuerzaLunar);
        movimientosGardevoir.add(psiquico);
        movimientosGardevoir.add(hipnosis);
        Pokemon gardevoir = new Pokemon(340, 340, 251, 251, 284, 383, 361, "Gardevoir", tipoPsiquico, tipoHada, movimientosGardevoir);
        pokemons.add(gardevoir);

        List<Movimiento> movimientosWobbuffet = new ArrayList<>();
        movimientosWobbuffet.add(corpulencia);
        movimientosWobbuffet.add(psiquico);
        movimientosWobbuffet.add(cabezazoZen);
        movimientosWobbuffet.add(hipnosis);
        Pokemon wobbuffet = new Pokemon(584, 584, 181, 236, 181, 181, 236, "Wobbuffet", tipoPsiquico, null, movimientosWobbuffet);
        pokemons.add(wobbuffet);

        List<Movimiento> movimientosGolem = new ArrayList<>();
        movimientosGolem.add(terremoto);
        movimientosGolem.add(ataqueArena);
        movimientosGolem.add(rocaAfilada);
        movimientosGolem.add(poderPasado);
        Pokemon golem = new Pokemon(364, 364, 372, 394, 207, 229, 251, "Golem", tipoRoca, tipoTierra, movimientosGolem);
        pokemons.add(golem);

        List<Movimiento> movimientosTyranitar = new ArrayList<>();
        movimientosTyranitar.add(triturar);
        movimientosTyranitar.add(pulsoUmbrio);
        movimientosTyranitar.add(poderPasado);
        movimientosTyranitar.add(pulimento);
        Pokemon tyranitar = new Pokemon(404, 404, 403, 350, 243, 317, 328, "Tyranitar", tipoRoca, tipoSiniestro, movimientosTyranitar);
        pokemons.add(tyranitar);

        List<Movimiento> movimientosAggron = new ArrayList<>();
        movimientosAggron.add(rocaAfilada);
        movimientosAggron.add(pulimento);
        movimientosAggron.add(garraMetal);
        movimientosAggron.add(focoResplandor);
        Pokemon aggron = new Pokemon(344, 344, 350, 504, 218, 240, 240, "Aggron", tipoAcero, tipoRoca, movimientosAggron);
        pokemons.add(aggron);

        List<Movimiento> movimientosMetagross = new ArrayList<>();
        movimientosMetagross.add(psiquico);
        movimientosMetagross.add(cabezazoZen);
        movimientosMetagross.add(garraMetal);
        movimientosMetagross.add(ecoMetalico);
        Pokemon metagross = new Pokemon(364, 364, 405, 394, 262, 317, 306, "Metagross", tipoAcero, tipoPsiquico, movimientosMetagross);
        pokemons.add(metagross);

        
        try{
            ObjectOutputStream archivo = new ObjectOutputStream(new FileOutputStream("pokemones.bin"));

            archivo.writeObject(pokemons);
            archivo.close();

        }catch (IOException e) {
            System.out.println("Error al guardar los pokemones en el archivo: " + e.getMessage());
        }
    }

    /**
     * Carga una lista de movimientos desde un archivo binario
     * @param nombreArchivo Nombre del archivo desde donde se cargarán los movimientos
     * @return Una lista con los movimientos cargados
     * @throws IOException Si ocurre un error durante la lectura del archivo
     * @throws ClassNotFoundException Si no se encuentra la clase adecuada durante la deserialización
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Movimiento> cargarMovimientos(String archivo) {
        Map<String, Movimiento> movimientos = new HashMap<>();
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            // Ahora cargamos directamente un mapa
            movimientos = (Map<String, Movimiento>) in.readObject();
            System.out.println("Movimientos cargados exitosamente desde: " + archivo);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar los movimientos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return movimientos;
    }
}
