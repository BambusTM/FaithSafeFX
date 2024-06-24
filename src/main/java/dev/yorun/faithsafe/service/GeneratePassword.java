package dev.yorun.faithsafe.service;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.passay.IllegalCharacterRule;

public class GeneratePassword {

  public String generatePassword() {
    PasswordGenerator gen = new PasswordGenerator();
    CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
    CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
    lowerCaseRule.setNumberOfCharacters(2);

    CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
    CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
    upperCaseRule.setNumberOfCharacters(2);

    CharacterData digitChars = EnglishCharacterData.Digit;
    CharacterRule digitRule = new CharacterRule(digitChars);
    digitRule.setNumberOfCharacters(2);

    CharacterData specialChars = new CharacterData() {
      public String getErrorCode() {
        return IllegalCharacterRule.ERROR_CODE;
      }

      public String getCharacters() {
        return "!@#$%^&*()_+[]|{}";
      }
    };
    CharacterRule splCharRule = new CharacterRule(specialChars);
    splCharRule.setNumberOfCharacters(2);

    String password = gen.generatePassword(10, splCharRule, lowerCaseRule,
        upperCaseRule, digitRule);
    return password;
  }
}
