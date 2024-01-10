package slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.model.block.composition.PlainTextObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SlackIntegrationTest {

    private static final String slackChannelId = "C06500TTZ0S";
    private static final String oAuthToken = "xoxb-6169917447459-6278571299890-9ozcXDqJv8uKZtBvA3YEBMj1";

    public static void main(String[] args) {
        System.out.println("Before");
        Slack slack = Slack.getInstance();

        MethodsClient methods = slack.methods(oAuthToken);

        List<LayoutBlock> layoutBlocks = sendMessageToSlack(":wave: William Frank requests CREDIT approval for a $1.00 USD AdvisorShares swap with 1 component. *<https://www.google.com/|view request>*");

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(slackChannelId)
                .text(":wave: William Frank requests CREDIT approval for a $1.00 USD AdvisorShares swap with 1 component.")
                .blocks(layoutBlocks)
                .build();

        ChatPostMessageResponse response = null;
        try {
            response = methods.chatPostMessage(request);
            if (response.isOk()) {
                System.out.println("Message sent to the slack channel");
            } else {
                System.out.println("Failed to send message in slack channel: " + response.getErrors());
            }
        } catch (IOException | SlackApiException e) {
            System.err.println("Error sending message to slack: " + e.getMessage());
        }

        System.out.println("After");
    }

    public static List<LayoutBlock> sendMessageToSlack(String slackMessage) {

        // Message Section Block
        LayoutBlock messageSection = Blocks.section(section -> section.text(
                BlockCompositions.markdownText(slackMessage)
        ));

        // Header Section Block
        LayoutBlock headerSection = Blocks.header(header -> header
                .text(PlainTextObject.builder().text("Swap Configurations").build())
                .blockId("headerBlockId"));
        LayoutBlock dividerBlock1 = Blocks.divider();

        LayoutBlock informationSection = Blocks.section(section -> section.fields(Arrays.asList(
                BlockCompositions.markdownText("*Trader:* Joseph Vencil"),
                BlockCompositions.markdownText("*Pricing Date:* 2023-11-24"),
                BlockCompositions.markdownText("*Client:* Nokomis capital master"),
                BlockCompositions.markdownText("*Target Notional:* 2000 USD"),
                BlockCompositions.markdownText("*Account:* Test account123"),
                BlockCompositions.markdownText("*Termination Date:* 2023-11-30"),
                BlockCompositions.markdownText("*Client trade direction:* BUY"),
                BlockCompositions.markdownText("*Underlying reset frequency:* " + Period.MONTHLY),
                BlockCompositions.markdownText("*Underlying payer:* clear street"),
                BlockCompositions.markdownText("*rate reset frequency:* Monthly")
        )));

        LayoutBlock additionalInformationSection = Blocks.section(section -> section.fields(Arrays.asList(
                BlockCompositions.markdownText("*Floating rate prayer:* Client"),
                BlockCompositions.markdownText("*Valuation Dates:* 1st business day of period"),
                BlockCompositions.markdownText("*Underlying type:* Equity"),
                BlockCompositions.markdownText("*Requested reference rate:* "),
                BlockCompositions.markdownText("*Underlying structure:* Single"),
                BlockCompositions.markdownText("*Requested spread to RR:* 0 bps")
        )));

        LayoutBlock actionButtonBlock = Blocks.actions(Arrays.asList(
                ButtonElement.builder()
                        .text(BlockCompositions.plainText("View Order"))
                        .style("primary")
                        .value("view order")
                        .url("https://dev-swaps-frontend-gateway-service.co.clearstreet.io/pricing-request/6")
                        .build()
        ));

        return Arrays.asList(messageSection, headerSection, dividerBlock1, informationSection, additionalInformationSection);
    }
}
