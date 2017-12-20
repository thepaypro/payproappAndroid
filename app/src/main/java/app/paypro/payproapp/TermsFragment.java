package app.paypro.payproapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by kike on 19/12/17.
 */

public class TermsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.terms_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView toolbarTitle = getActivity().findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText("Terms & Conditions");

        TextView toolbar_back_button_text = getActivity().findViewById(R.id.app_toolbar_back_button_text);
        toolbar_back_button_text.setText("Info");
        toolbar_back_button_text.setVisibility(View.VISIBLE);

        Button confirmButton = getActivity().findViewById(R.id.app_toolbar_confirm_button);
        confirmButton.setVisibility(View.INVISIBLE);

        ProgressBar progressBar = getActivity().findViewById(R.id.app_toolbar_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        ImageButton toolbar_back_button_image = getActivity().findViewById(R.id.app_toolbar_back_button_image);
        toolbar_back_button_image.setVisibility(View.VISIBLE);
        toolbar_back_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.frame_layout, new InfoFragment());
                transaction.commit();
            }
        });

        TextView terms = getView().findViewById(R.id.terms_text);
        terms.setText(Html.fromHtml("<p>This is a binding Agreement between THEPAYPRO LTD. (“PayPro App” or “We”) and the person, persons, or entity (“You” or “Your”) using the service, Software, or application (“Software”).</p>\n" +
                "<p><b>RIGHTS AND OBLIGATIONS</b></p>\n" +
                "<p>PayPro App provides the Software solely on the terms and conditions set forth in this Agreement and on the condition that You accept and comply with them. By using the Software You (a) accept this Agreement and agree that You are legally bound by its terms; and (b) represent and warrant that: (i) You are of legal age to enter into a binding agreement; and (ii) if You are a corporation, governmental organization or other legal entity, You have the right, power and authority to enter into this Agreement on behalf of the corporation, governmental organization or other legal entity and bind them to these terms.</p>\n" +
                "<p>This Software functions as a free, open source, and multi-signature digital wallet. The Software does not constitute an account where We or other third parties serve as financial intermediaries or custodians of Your bitcoin(s).</p>\n" +
                "<p>While the Software has undergone beta testing and continues to be improved by feedback from the open-source user and developer community, We cannot guarantee there will not be bugs in the Software. You acknowledge that Your use of this Software is at Your own discretion and in compliance with all applicable laws. You are responsible for safekeeping Your passwords, private key pairs, PINs, and any other codes You use to access the Software.</p>\n" +
                "<p>IF YOU LOSE ACCESS TO YOUR BITCOIN WALLET OR YOUR ENCRYPTED PRIVATE KEYS AND YOU HAVE NOT SEPARATELY STORED A BACKUP OF YOUR WALLET AND CORRESPONDING PASSWORD, YOU ACKNOWLEDGE AND AGREE THAT ANY BITCOIN YOU HAVE ASSOCIATED WITH THAT WALLET WILL BECOME INACCESSIBLE. All transaction requests are irreversible. The authors of the Software, employees and affiliates of PayPro App, copyright holders, cannot guarantee transaction confirmation as they do not have control over the bitcoin network.</p>\n" +
                "<p><b>DISCLAIMER</b></p>\n" +
                "<p>THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OF THE SOFTWARE, EMPLOYEES AND AFFILIATES OF PAYPRO APP, COPYRIGHT HOLDERS, OR PAYPRO APP, INC. BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.</p>\n" +
                "<p>IN NO EVENT WILL PAYPRO APP OR ITS AFFILIATES, OR ANY OF ITS OR THEIR RESPECTIVE SERVICE PROVIDERS, BE LIABLE TO YOU OR ANY THIRD PARTY FOR ANY USE, INTERRUPTION, DELAY OR INABILITY TO USE THE SOFTWARE, LOST REVENUES OR PROFITS, DELAYS, INTERRUPTION OR LOSS OF SERVICES, BUSINESS OR GOODWILL, LOSS OR CORRUPTION OF DATA, LOSS RESULTING FROM SYSTEM OR SYSTEM SERVICE FAILURE, MALFUNCTION OR SHUTDOWN, FAILURE TO ACCURATELY TRANSFER, READ OR TRANSMIT INFORMATION, FAILURE TO UPDATE OR PROVIDE CORRECT INFORMATION, SYSTEM INCOMPATIBILITY OR PROVISION OF INCORRECT COMPATIBILITY INFORMATION OR BREACHES IN SYSTEM SECURITY, OR FOR ANY CONSEQUENTIAL, INCIDENTAL, INDIRECT, EXEMPLARY, SPECIAL OR PUNITIVE DAMAGES, WHETHER ARISING OUT OF OR IN CONNECTION WITH THIS AGREEMENT, BREACH OF CONTRACT, TORT (INCLUDING NEGLIGENCE) OR OTHERWISE, REGARDLESS OF WHETHER SUCH DAMAGES WERE FORESEEABLE AND WHETHER OR NOT WE WERE ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.</p>\n" +
                "<p><b>INTELLECTUAL PROPERTY</b></p>\n" +
                "<p>We retain all right, title, and interest in and to the Content and all of PayPro App’s brands, logos, and trademarks and variations of the wording of the aforementioned brands, logos, and trademarks.</p>\n" +
                "<p><b>CHOICE OF LAW</b></p>\n" +
                "<p>This Agreement, and its application and interpretation, shall be governed exclusively by the laws of the United Kingdom, without regard to its conflict of law rules.</p>\n" +
                "<p><b>SEVERABILITY</b></p>\n" +
                "<p>In the event any court shall declare any section or sections of this Agreement invalid or void, such declaration shall not invalidate the entire Agreement and all other paragraphs of the Agreement shall remain in full force and effect.</p>\n" +
                "<p><b>BINDING AGREEMENT</b></p>\n" +
                "<p>The terms and provisions of this Agreement are binding upon Your heirs, successors, assigns, and other representatives. This Agreement may be executed in counterparts, each of which shall be considered to be an original, but both of which constitute the same Agreement.</p>\n" +
                "<p>You assume any and all risks associated with the use of the Software. We reserve the right to modify this Agreement from time to time.</p>"));
    }
}
