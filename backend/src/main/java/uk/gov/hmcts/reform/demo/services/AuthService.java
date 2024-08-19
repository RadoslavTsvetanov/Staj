package uk.gov.hmcts.reform.demo.services;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.demo.mocks.TokenProviderMocker;

import java.util.HashMap;
import java.util.Map;
@Service
public class AuthService {
    private Map<String,String> tokens = new HashMap<>();
    private TokenProviderMocker provider = new TokenProviderMocker();

    public AuthService() {

    }

    public String issueToken(String id){
        String token = provider.provideToken(id);
        tokens.put(token,id);

        return token;
    }

    public Boolean checkToken(String token, String id){

        return (tokens.get(token) == id);
    }

    public Boolean checkIfTokenExists(String token){
        return tokens.containsKey(token);
    }
}
