import it.koros.producer.utils.SecurityUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecurityUtilTest {

    @Test
    void testHmacGenerationAndValidation() {
        String payload = "test-message";
        String hmac = SecurityUtil.generateHmac(payload);
        assertTrue(SecurityUtil.verifyHmac(payload, hmac));
    }

    @Test
    void testJwtGenerationAndValidation() {
        String payload = "event:USER_CREATED";
        String token = SecurityUtil.generateJwt(payload);
        assertTrue(SecurityUtil.verifyJwt(token));
    }

    @Test
    void testInvalidJwtShouldFail() {
        String fakeJwt = "invalid.jwt.token";
        assertFalse(SecurityUtil.verifyJwt(fakeJwt));
    }
}