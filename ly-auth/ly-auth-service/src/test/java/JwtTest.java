import cn.x5456.leyou.auth.entity.UserInfo;
import cn.x5456.leyou.auth.utils.JwtUtils;
import cn.x5456.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "/Users/x5456/Desktop/ly-parent/rsa.pub";

    private static final String priKeyPath = "/Users/x5456/Desktop/ly-parent/rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {

        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {

        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU0MjYwNjY4OX0.HqdIZPk1FAgMdpJrPM-LMQgcRMj5DPan4IrJNIQ0wSdxNBIhw4FRRH5LvIuyZZuvRjY1o3zokSXesz79qH_jvFoko5duFfzqnBVonQWaaSzi96Fi4qRWfWSH9iAjn6T5i-yZ9miGWU-tBBsbUwnms688W4FijvWrGUY2QG8NAY4";
        // 解析token
        UserInfo user = JwtUtils.getUserInfo(publicKey,token);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}