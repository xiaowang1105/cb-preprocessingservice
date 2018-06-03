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
@Ignore
public class WordVectorizerTest {
    private WordVectorizer vectorizer;
    private final List<Float> VECTOR_OF_1 = Stream.of(-0.06392972,-0.014201283,0.38877043,0.11120229,0.15128377,0.116269104,-0.11005489,0.03577683,-0.21741746,0.14069818,-0.026515132,-0.032305483,0.093705155,0.010609114,0.030671462,0.106514916,-0.020832991,0.057193916,-0.11171616,-0.0051404806,0.11104972,-0.06506636,-0.02801152,-0.061201833,0.009896916,-0.03893322,0.044416666,-0.06340964,0.05097345,0.09646411,0.01495266,-0.048837703,-0.09961323,0.12862007,0.22063531,0.005463785,0.018761996,-0.14925613,0.10088354,-0.048683073,-0.09053071,0.05307966,-0.048278723,0.006608123,-0.093698174,-0.08405377,0.089441545,0.005282337,-0.088154525,-0.032938916,0.027990572,0.018231314,0.06331049,-0.67513263,-0.0186109,0.0456491,0.18340836,-0.0066471305,-0.103978194,-0.039883908,-0.04769248,0.02745544,-0.041417908,0.118589915,-0.10582659,0.011367446,-0.1272174,0.092308715,-0.050726384,0.02267898,0.079113424,-0.028538777,0.0212044,0.0389549,-0.040757705,0.048340656,0.054855883,-0.017848982,-0.021933768,0.07822574,-0.0019785748,-0.008300417,0.060264666,0.094418734,0.04258183,0.0018320126,-0.11896725,0.034632802,0.016871098,0.15301365,-0.04450779,-0.008738441,0.002495461,-0.05580242,0.033419214,-0.026466865,0.017639274,-0.055797085,-0.07434373,0.070910536,-0.15464747,0.018318407,0.08285258,0.0038693715,0.321264,-0.0064339186,0.0880748,-0.03963387,-0.06929173,-0.023441099,0.056897856,0.1239123,-0.10739054,0.018559752,0.17762126,0.12714681,0.0076508126,-0.063148916,-0.055052083,-0.06452098,0.13295877,0.12311168,-0.16859676,0.0930112,-0.12324241,0.07191908,-0.09243157,-0.09220072,-0.08468337,0.12400639,0.1823847,-0.08106127,1.8744807E-4,0.073939614,0.07169485,0.020073619,-0.103034355,0.08712282,-0.078305006,-0.03647212,0.034745287,-0.0520734,-0.06476955,0.14439438,-0.006470288,0.07837611,0.031283002,0.06832126,0.007863673,0.074559964,0.031532466,0.04334345,0.063562825,0.052917458,0.20200896,0.018244592,-0.1566654,-0.032210086,0.08945653,-0.25672108,0.023426488,0.024186349,-0.08653737,0.0034261467,0.13878143,0.031169545,0.0013030396,0.044381145,0.006723364,-0.042806406,0.06990613,-0.026225869,0.11555056,0.08006663,-0.05573782,0.035442792,0.030441444,0.021776298,0.018941062,-0.21549648,-0.07298658,0.5971621,0.024025476,-0.01767563,-0.03425941,0.07496687,0.10119536,0.15146893,-0.09052267,0.17970948,-0.097692795,-0.43171057,-0.138924,-0.07384316,0.10064043,0.012216928,-0.011986996,0.03624851,-0.095052175,0.14754899,0.033558033,-0.09082427,-0.09665685,-0.015211856,0.029806668,-0.033306953,0.0140948845,-0.021301206,0.053862493,0.051116124,0.010550859,-0.017863704,0.00148129,-0.105908506,0.083368964,0.041957974,0.11780982,-0.049369667,0.07285312,-0.019336374,0.044651013,-0.14348014,0.024199402,-0.015571579,0.22242106,-0.0074425805,0.074472174,0.05853535,-0.05838404,0.09756875,-0.17171423,0.48304728,0.02141379,-0.08278466,-0.0063823257,0.15107273,-0.061275084,-0.010797352,-0.034203574,-0.114782006,0.1526532,0.06799573,-0.09768605,-0.051233385,-0.08021838,0.04336004,-0.062401783,0.048101965,0.0074053514,-0.006863201,0.0742497,0.012491669,0.12780373,-0.040066946,-0.11133901,-0.076859586,-0.11034391,-0.08933993,0.09385141,-0.08999312,-0.15074548,0.016182775,0.08572729,-0.069176435,0.10795495,0.0146569805,0.010509318,0.11343491,-0.0616988,-0.057414774,-0.13134904,-0.053007323,-0.044948984,-0.029926727,-0.028712075,-0.09899328,0.0645978,-0.11647171,-0.046278246,0.009197063,-0.29163575,-0.023739878,-0.016439136,0.2806302,-0.06659674,-0.07013841,0.016884245,0.055336636,-0.24205105,-0.0082404185,0.064689636,-0.09747183,0.048468627,0.029082038,0.026858598,-0.09394914,0.10642726,0.0373256,-0.01604899,0.10587212).map(Double::floatValue).collect(Collectors.toList());
    private final List<Float> EMPTY_VECTOR = Collections.nCopies(300, 0f);

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
        assertEquals(Arrays.asList(VECTOR_OF_1, VECTOR_OF_1, EMPTY_VECTOR, EMPTY_VECTOR), vectorizer.getVectors(Arrays.asList("1", "1"), 4));
    }
}