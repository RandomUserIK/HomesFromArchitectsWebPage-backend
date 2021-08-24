package sk.hfa.google.products.domain;

import com.google.api.client.util.Key;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantInfo {

    @Key
    private BigInteger merchantId;

    @Key
    private String accountSampleUser;

}
