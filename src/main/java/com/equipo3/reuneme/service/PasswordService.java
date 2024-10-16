package com.equipo3.reuneme.service;

import java.util.ArrayList;
import java.util.List;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {
	
	public boolean isValid(String pwd) {
		        
				//Lista de reglas(requisitos) de contraseña segura
		        List<Rule> rules = new ArrayList<>();
		        
		        //Regla 1: Contraseña mínimo 8 caracteres
		        //Máximo definido para BD en clase Usuario
		        rules.add(new LengthRule(8, 128));
		        
		        //Regla 2: No se admite espacios
		        rules.add(new WhitespaceRule());
		        
		        //Regla 3: Debe contener una mayuscula
		        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
		        
		        //Regla 4: Debe contener una minuscula
		        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));  
		        
		        //Regla 5: Debe contener un dígito
		        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));        
		        
		        //Regla 6: Debe contener un caracter especial
		        rules.add(new CharacterRule(EnglishCharacterData.Special, 1));
		        
		        //Se añaden las reglas al validador
		        PasswordValidator validator = new PasswordValidator(rules);
		        
		        PasswordData password = new PasswordData(pwd);        
		        RuleResult result = validator.validate(password);
		        
		        return result.isValid();
	}
	
	public boolean isSamePwd(String pwd1, String pwd2) {
		return pwd1.equals(pwd2);
	}
}
