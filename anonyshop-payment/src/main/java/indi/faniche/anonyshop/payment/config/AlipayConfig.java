package indi.faniche.anonyshop.payment.config;

/* File:   AlipayConfig.java
 * -------------------------
 * Author: faniche
 * Date:   5/4/20
 */

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {

    private String alipay_url = "https://openapi.alipaydev.com/gateway.do";

    private String app_private_key = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDjKmQG1OLmiiSosaRCH5VEnHU7g/AteXiW7eBosZw3L5yI5v+jAkKDd2pit5AjGbB/OuSjlH4t8FJUbMwpH4/xOXZeidzzuaNQpsQzFI4f6jQ3DGeioc7VLRD7OchWpfw3llt0Hrfxtg3HXvp2ox/QsijZ5UfGIAVp3LxefrQsi/OGeVArJrs/MDBgEPm9cdoTAm7UC/edhPot+sudWaT3B0chaemNnCUGInPUxPznUl/QzqtAtdVBTOuf20XOEMPZcfz419QRRfCCQ/QKzmMgfuIiIa+No9nbVLYk0uD33/JzIWr4WC5k16IOkN2yKnluRa/DGx+Thd7c8LB8GSXfAgMBAAECggEBAMoIM3nX8Ay1I7FcfAfK6whgVesehocLF3K0EPdLrvFtyLwBJa4mHl2p4NX3B5wQBubAzAOn1yioKbCoKlMQnQYe4uVlmzqwCU17Y9dCGrzLoLCsuabsXXU+ZXHpjAF0EgKmGKQHhfTUwyBjmkHVBkCnkqlnvxO3+tiR1n38FeXezOIfECIWfzy4sVGIorBTN0pEEsFB3m4Z3CDdqImTJJM2U7paYA2MmKr4uV75u9w9PAVj5Io+QGrimruqZgpTvcWq7ndlRvgoJffSXGVfzWLqmeoj0qoXsMRMfPG1mRcXGa6AMyEGJOkrr1uqvJSz1S3ELnI4N6Q5MajdgLgbLiECgYEA+zkd5giAnNHAt1S18jjhsdcFabGEGFm9LT6zuGyJNr88Y0mSjDOUP2f+3rwqS3c5T6U4pa/Jf5PWGvDe5ZHFyGmzBHcEfEPdlfLuxTe7+xvob2LbwLZuhpQAiJy92InfYTd3HgLXoQFpiffcH6CennUN+mapcuv150LY1+PyZIcCgYEA53wrMBV2AGgBz1BNs+DpniyZ4pSb/U6edfMbx6h5lG2OnfPWFtK3EClvEGt4YMokAdliGSVJObZXLQgJKsexAtOg+wtabQcUit+emdOtWUZWh70CC2qQGSK14T1DnwYymWC6Ai6RZpvdyuU3DBfIBPSOVEaM/D8zmeHdQ/4X4ekCgYACW4PbT8MdNZ6wWbwnXL9JcvIbDs5liBoNXaREG8ih2eOQLvWQGT/P5ILwxiVDdgf0pmFEtMSYTOPadt4wm1CW098ZMKC7Jcvjh1cURLSf46Anw266fTvGSYdDwOzLJmgROnsErhQl76R05f2L0m8Kn6CU8/etG2fAVtFAHWR96wKBgQCoSq+jT5bwiKz4Kau0IMn3gvmFrwERUb0Gh24k46kwbc4Hq6R0B1Gr4FOsnlVwGMXAymf1XBQ2bwhF5tezUGayZWVd3k0pgsI+jIDj2O7oPqjeGl2IKzsD6yZL0Jx+2qSiuZ3BxFVVQfAneCljE08JuzpWrswE/3XmZDzXF74JMQKBgQDk+dotEwcuIoqy7fZuK+p14KLCHGs43z3rACDhJVFr77SI9n5QA/RnbCQL+2DZgKrRtBihy75EeA8YKLnr3gGvKP/e2GHWHgtw3pscdHjw76kHrCs3kk5iqjjKRs7v/5sw6V/ucS1u0ILFsSfDyIXyVMyr8sfzzJTfL7S7KgK0xA==";

    private String app_id = "2016102300744151";

    public final static String format = "json";

    public final static String charset = "utf-8";

    public final static String sign_type = "RSA2";

    // 支付宝公钥证书
    public static String alipay_cert_path = "/home/faniche/Documents/Config/alipay/alipayCertPublicKey_RSA2.crt";
    // 支付宝CA根证书文件
    public static String alipay_root_cert_path = "/home/faniche/Documents/Config/alipay/alipayRootCert.crt";
    // 应用公钥证书
    public static String app_cert_path = "/home/faniche/Documents/Config/alipay/appCertPublicKey_2016102300744151.crt";

    public static String return_payment_url = "http://payment.anonyshop.tech/alipay/success";

    public static String notify_payment_url = "http://121.36.66.5/alipay/callback/notify";

    public static String return_order_url = "http://order.anonyshop.tech/orderList";

    @Bean
    public AlipayClient alipayClient() throws AlipayApiException {
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(alipay_url);
        certAlipayRequest.setAppId(app_id);
        certAlipayRequest.setPrivateKey(app_private_key);
        certAlipayRequest.setFormat("json");
        certAlipayRequest.setCharset(charset);
        certAlipayRequest.setSignType(sign_type);
        certAlipayRequest.setCertPath(app_cert_path);
        certAlipayRequest.setAlipayPublicCertPath(alipay_cert_path);
        certAlipayRequest.setRootCertPath(alipay_root_cert_path);
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
        return alipayClient;
    }
}
