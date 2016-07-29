/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.examples;


import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.EncounterResult;
import com.pokegoapi.auth.GoogleLogin;
import com.pokegoapi.auth.PtcLogin;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;

import java.util.List;

public class Main {

	/**
	 * Catches a pokemon at an area.
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth = null;
		try {
			auth = new GoogleLogin(http).login("eyJhbGciOiJSUzI1NiIsImtpZCI6IjUwNzgyYmNmMGE5NzQxZTZiZjkwMjY2ZGMzNTY4YWE5MDc5MWYxNmYifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhdF9oYXNoIjoiUDc4bl9TQ0h2VDdCNWxSQ3ZrM0xBdyIsImF1ZCI6Ijg0ODIzMjUxMTI0MC03M3JpM3Q3cGx2azk2cGo0Zjg1dWo4b3RkYXQyYWxlbS5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwMzU0NjAwNDM0ODc0MDgyOTg3OCIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhenAiOiI4NDgyMzI1MTEyNDAtNzNyaTN0N3Bsdms5NnBqNGY4NXVqOG90ZGF0MmFsZW0uYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJlbWFpbCI6ImlraHNhbi5hdWxpYUBnbWFpbC5jb20iLCJpYXQiOjE0Njk1OTE2NTMsImV4cCI6MTQ2OTU5NTI1M30.rYJR92fq-IBnIbq_FI6ffR_uy4jZLZ522ibFS4PYu6Q0yYxsf_kfptq4zd5lBeCavjEnbDqRw0P0vR6g90f-hdI3bL_6jhniTdmldQd6AfHiawiOSf4Vbw7Fl8hNaTZSqPxJ3r7OdcvadgR9EyZThmOeCk3h4GD7k0zDGFeiOmwNRZy6NJ_MsM0RC3cuzof13H2-6HcgdHIgGdwTJsN0qIKHKIfPQpmKyzf28CLjxmyZkNqPCqMzV_qEUhcScY_qe3FG6EYK1xeMlohj1jM5WlqrH9Is_jiptRm_XCBTzoh-WWFYc_Sfav7a1F2-OF-fM3DfCxHXhjaoYa6gFjUCaQ");
			//auth = new PtcLogin(http).login(ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD);
			// or google
			//auth = new GoogleLogin(http).login("", ""); // currently uses oauth flow so no user or pass needed
			PokemonGo go = new PokemonGo(auth, http);
			// set location
			go.setLocation(-32.0580824,115.7421363, 0);

			List<CatchablePokemon> catchablePokemon = go.getMap().getCatchablePokemon();
			System.out.println("Total pokemon :" + catchablePokemon.size());

			for (CatchablePokemon cp : catchablePokemon) {
				
				EncounterResult encResult = cp.encounterPokemon();
				
				if (encResult.wasSuccessful()) {
					System.out.println("Ketemu:" + cp.getPokemonId());
					CatchResult result = cp.catchPokemonWithRazzBerry();
					System.out.println("Tangkap:" + cp.getPokemonId() + " " + result.getStatus());
				}

			}

		} catch (LoginFailedException | RemoteServerException e) {
			Log.e("Main", "Login failed: ", e);
		}
	}
}
