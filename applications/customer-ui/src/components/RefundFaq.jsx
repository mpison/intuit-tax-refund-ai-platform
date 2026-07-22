import {
    useState
} from "react";


const faqItems = [
    {
        id: "refund-status",
        question: "Where's my refund?",
        content: (
            <>
                <p>
                    You can start tracking your refund after the tax
                    authority confirms that your return has been accepted.
                </p>

                <p>
                    Refund processing usually takes about 21 days after
                    acceptance, although some returns may require additional
                    review.
                </p>

                <p>
                    Check your refund dashboard for the latest status,
                    estimated refund date, and recommended next steps.
                </p>

                <button
                    type="button"
                    className="faqPrimaryButton"
                    onClick={() => {
                        window.open("https://sa.www4.irs.gov/wmr/", "_blank");
                    }}
                >
                        View refund tracker
                </button>
            </>
        )
    },
    {
        id: "amend-return",
        question: "How do I amend my return?",
        content: (
            <>
                <p>
                    Amend a return when you need to add missing forms,
                    correct income, or update information from your original
                    filing.
                </p>

                <p>
                    An amended return follows a separate processing workflow
                    and may take longer than the original return.
                </p>

                <button
                    type="button"
                    className="faqPrimaryButton"
                    onClick={() => {
                       window.open("https://ttlc.intuit.com/turbotax-support/en-us/help-article/tax-return/need-amend/L4CbYvSN4_US_en_US", "_blank");
                    }}
                >
                    Amend your return
                </button>
            </>
        )
    },
    {
        id: "tax-notice",
        question: "I received a tax notice. What should I do?",
        content: (
            <>
                <p>
                    Review the notice carefully and confirm the response
                    deadline. A notice does not always mean that you owe
                    additional tax.
                </p>

                <p>
                    Use the support page to review the notice type and
                    determine the appropriate next step.
                </p>

                <button
                    type="button"
                    className="faqPrimaryButton"
                    onClick={() => {
                         window.open("https://ttlc.intuit.com/turbotax-support/en-us/help-article/tax-audit/turbotax-audit-support/L6AcMoNFD_US_en_US", "_blank");
                    }}
                >
                    View notice support
                </button>
            </>
        )
    }
];

export default function RefundFaq() {

    const [
        expandedFaqId,
        setExpandedFaqId
    ] = useState(
        faqItems[0].id
    );

    function toggleFaq(
        faqId) {

        setExpandedFaqId(
            currentFaqId =>
                currentFaqId === faqId
                    ? null
                    : faqId
        );
    }

    return (
        <aside
            className="refundFaqPanel"
            aria-label="Frequently asked questions"
        >
            <h2 className="refundFaqTitle">
                FAQs
            </h2>

            <div className="refundFaqList">
                {
                    faqItems.map(
                        faqItem => {

                            const isExpanded =
                                expandedFaqId === faqItem.id;

                            const contentId =
                                `faq-content-${faqItem.id}`;

                            return (
                                <section
                                    key={faqItem.id}
                                    className="refundFaqItem"
                                >
                                    <button
                                        type="button"
                                        className="refundFaqQuestion"
                                        aria-expanded={isExpanded}
                                        aria-controls={contentId}
                                        onClick={() =>
                                            toggleFaq(
                                                faqItem.id
                                            )
                                        }
                                    >
                                        <span>
                                            {faqItem.question}
                                        </span>

                                        <span
                                            className={
                                                isExpanded
                                                    ? "refundFaqChevron expanded"
                                                    : "refundFaqChevron"
                                            }
                                            aria-hidden="true"
                                        >
                                           ⌄
                                        </span>
                                    </button>

                                    <div
                                        id={contentId}
                                        className={
                                            isExpanded
                                                ? "refundFaqContent expanded"
                                                : "refundFaqContent"
                                        }
                                    >
                                        <div className="refundFaqContentInner">
                                            {faqItem.content}
                                        </div>
                                    </div>
                                </section>
                            );
                        }
                    )
                }
            </div>

            <section className="taxInfoSection">
                <h2>
                    My tax info
                </h2>

                <div className="taxInfoRow">
                    <span>
                        My adjusted gross income (AGI)
                    </span>

                    <strong>
                        $97,872
                    </strong>
                </div>

                <a href="#">
                    Past tax returns and documents
                </a>

                <a href="#">
                    Your order details
                </a>
            </section>
        </aside>
    );
}