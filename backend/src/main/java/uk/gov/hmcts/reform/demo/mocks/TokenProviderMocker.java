package uk.gov.hmcts.reform.demo.mocks;

import java.util.UUID;

public class TokenProviderMocker {
    public String provideToken(String param){
        return param+"1";
    }
}
