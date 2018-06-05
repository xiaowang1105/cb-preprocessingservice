package cn.azure.chatbot.preprocessingservice;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

// Test needs models under repo root, and they're too slow
@Ignore("Too slow and needs too much memory")
public class WordVectorizerTest {
    private WordVectorizer vectorizer;
    private final List<Float> VECTOR_OF_1 = Arrays.asList(-0.06392972f,-0.014201283f,0.38877043f,0.11120229f,0.15128377f,0.116269104f,-0.11005489f,0.03577683f,-0.21741746f,0.14069818f,-0.026515132f,-0.032305483f,0.093705155f,0.010609114f,0.030671462f,0.106514916f,-0.020832991f,0.057193916f,-0.11171616f,-0.0051404806f,0.11104972f,-0.06506636f,-0.02801152f,-0.061201833f,0.009896916f,-0.03893322f,0.044416666f,-0.06340964f,0.05097345f,0.09646411f,0.01495266f,-0.048837703f,-0.09961323f,0.12862007f,0.22063531f,0.005463785f,0.018761996f,-0.14925613f,0.10088354f,-0.048683073f,-0.09053071f,0.05307966f,-0.048278723f,0.006608123f,-0.093698174f,-0.08405377f,0.089441545f,0.005282337f,-0.088154525f,-0.032938916f,0.027990572f,0.018231314f,0.06331049f,-0.67513263f,-0.0186109f,0.0456491f,0.18340836f,-0.0066471305f,-0.103978194f,-0.039883908f,-0.04769248f,0.02745544f,-0.041417908f,0.118589915f,-0.10582659f,0.011367446f,-0.1272174f,0.092308715f,-0.050726384f,0.02267898f,0.079113424f,-0.028538777f,0.0212044f,0.0389549f,-0.040757705f,0.048340656f,0.054855883f,-0.017848982f,-0.021933768f,0.07822574f,-0.0019785748f,-0.008300417f,0.060264666f,0.094418734f,0.04258183f,0.0018320126f,-0.11896725f,0.034632802f,0.016871098f,0.15301365f,-0.04450779f,-0.008738441f,0.002495461f,-0.05580242f,0.033419214f,-0.026466865f,0.017639274f,-0.055797085f,-0.07434373f,0.070910536f,-0.15464747f,0.018318407f,0.08285258f,0.0038693715f,0.321264f,-0.0064339186f,0.0880748f,-0.03963387f,-0.06929173f,-0.023441099f,0.056897856f,0.1239123f,-0.10739054f,0.018559752f,0.17762126f,0.12714681f,0.0076508126f,-0.063148916f,-0.055052083f,-0.06452098f,0.13295877f,0.12311168f,-0.16859676f,0.0930112f,-0.12324241f,0.07191908f,-0.09243157f,-0.09220072f,-0.08468337f,0.12400639f,0.1823847f,-0.08106127f,1.8744807E-4f,0.073939614f,0.07169485f,0.020073619f,-0.103034355f,0.08712282f,-0.078305006f,-0.03647212f,0.034745287f,-0.0520734f,-0.06476955f,0.14439438f,-0.006470288f,0.07837611f,0.031283002f,0.06832126f,0.007863673f,0.074559964f,0.031532466f,0.04334345f,0.063562825f,0.052917458f,0.20200896f,0.018244592f,-0.1566654f,-0.032210086f,0.08945653f,-0.25672108f,0.023426488f,0.024186349f,-0.08653737f,0.0034261467f,0.13878143f,0.031169545f,0.0013030396f,0.044381145f,0.006723364f,-0.042806406f,0.06990613f,-0.026225869f,0.11555056f,0.08006663f,-0.05573782f,0.035442792f,0.030441444f,0.021776298f,0.018941062f,-0.21549648f,-0.07298658f,0.5971621f,0.024025476f,-0.01767563f,-0.03425941f,0.07496687f,0.10119536f,0.15146893f,-0.09052267f,0.17970948f,-0.097692795f,-0.43171057f,-0.138924f,-0.07384316f,0.10064043f,0.012216928f,-0.011986996f,0.03624851f,-0.095052175f,0.14754899f,0.033558033f,-0.09082427f,-0.09665685f,-0.015211856f,0.029806668f,-0.033306953f,0.0140948845f,-0.021301206f,0.053862493f,0.051116124f,0.010550859f,-0.017863704f,0.00148129f,-0.105908506f,0.083368964f,0.041957974f,0.11780982f,-0.049369667f,0.07285312f,-0.019336374f,0.044651013f,-0.14348014f,0.024199402f,-0.015571579f,0.22242106f,-0.0074425805f,0.074472174f,0.05853535f,-0.05838404f,0.09756875f,-0.17171423f,0.48304728f,0.02141379f,-0.08278466f,-0.0063823257f,0.15107273f,-0.061275084f,-0.010797352f,-0.034203574f,-0.114782006f,0.1526532f,0.06799573f,-0.09768605f,-0.051233385f,-0.08021838f,0.04336004f,-0.062401783f,0.048101965f,0.0074053514f,-0.006863201f,0.0742497f,0.012491669f,0.12780373f,-0.040066946f,-0.11133901f,-0.076859586f,-0.11034391f,-0.08933993f,0.09385141f,-0.08999312f,-0.15074548f,0.016182775f,0.08572729f,-0.069176435f,0.10795495f,0.0146569805f,0.010509318f,0.11343491f,-0.0616988f,-0.057414774f,-0.13134904f,-0.053007323f,-0.044948984f,-0.029926727f,-0.028712075f,-0.09899328f,0.0645978f,-0.11647171f,-0.046278246f,0.009197063f,-0.29163575f,-0.023739878f,-0.016439136f,0.2806302f,-0.06659674f,-0.07013841f,0.016884245f,0.055336636f,-0.24205105f,-0.0082404185f,0.064689636f,-0.09747183f,0.048468627f,0.029082038f,0.026858598f,-0.09394914f,0.10642726f,0.0373256f,-0.01604899f,0.10587212f);

    @Before
    public void setUp() throws Exception {
        vectorizer=new WordVectorizer("cc.zh.300.bin");
    }

    @Test
    public void testGetDimension() {
        assertEquals(300, vectorizer.getDimension());
    }

    @Test
    public void testGetWordVector() {
        assertEquals(VECTOR_OF_1, vectorizer.getWordVector("1"));
    }

    @Test
    public void testGetVectors() {
        // seqlen limits the length of output sequence
        assertEquals(Arrays.asList(VECTOR_OF_1, VECTOR_OF_1, VECTOR_OF_1), vectorizer.getVectors(Arrays.asList("1", "1", "1", "1"), 3));
    }
}