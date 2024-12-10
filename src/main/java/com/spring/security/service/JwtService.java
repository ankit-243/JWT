package com.spring.security.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {
 
	//Generating the token code --------------------------------------------------------
	// to generate the secret key we can use key generator

	public static String generateSecretKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256"); // we have to pass the algorithm
		SecretKey secretKey = keyGen.generateKey();
		return Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}

	public  String generateToken(String usnername) throws InvalidKeyException, DecodingException, NoSuchAlgorithmException {
   
		// we have to set the claims : claims are headers only
		Map<String, Object> claims = new HashMap<>();

		// now lets the jwts : claims are headers here , subject is the username or whom the token refers to(whose token it is)
		// , in signWith we have to mention secret key and the algo to create digital
		// signature
		//Jwts.parser().setSigningKey(generateSecretKey()).parseClaimsJwt("q34939342").getBody().getExpiration()
		return Jwts.builder().setClaims(claims).setSubject(usnername).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000*60*10))
				.signWith(SignatureAlgorithm.HS256, generateSecretKey()).compact();

	}
	

	// to generate the key , we have to also pass the secret key
	private static Key getKey() throws DecodingException, NoSuchAlgorithmException {
		byte[] keybytes=null;
		try {
			keybytes = Decoders.BASE64.decode(generateSecretKey());
		} catch (DecodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // creating the secret key
		return Keys.hmacShaKeyFor(keybytes); // creating the digital signature
	}


	//------------------------------------------------------------------------------------------------------------

	//to extract the username from the token
	public String extractUsername(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token,Claims::getSubject);
	}
	
	//create a method that can extract all the claims
	private <T> T extractClaim(String token,Function<Claims,T> claimResolver) {
		Claims claims=null;
		try {
			claims = extractAllClaims(token);
		} catch (SignatureException | DecodingException | ExpiredJwtException | UnsupportedJwtException
				| MalformedJwtException | IllegalArgumentException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return claimResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) throws SignatureException, DecodingException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, NoSuchAlgorithmException {
		return Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build().parseClaimsJws(token).getBody();
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractUsername(token);
		
		return userName.equals(userDetails.getUsername()) && !isTokenExprired(token);
	}

	private boolean isTokenExprired(String token) {
		
		return extractExpiration(token).before(new Date());//checking the expiration time with the current time
	}

	private Date extractExpiration(String token) {
		
		return extractClaim(token,Claims::getExpiration);
	}
	
}
