import sys
import re
from transformers import pipeline

def extract_command_blocks(log_file):
    with open(log_file, "r") as f:
        content = f.read()

    # Extract each command-output pair
    pattern = r"Command: (.*?)\nOutput:\n(.*?)(?=\nCommand: |\Z)"
    matches = re.findall(pattern, content, re.DOTALL)

    structured_actions = []
    for cmd, output in matches:
        cmd = cmd.strip()
        output = output.strip().replace('\n', ', ')
        if not output:
            output = "no visible output"
        structured_actions.append(f"The user ran '{cmd}' which gave: {output}.")

    return " ".join(structured_actions)

def summarize_session(text, output_file):
    summarizer = pipeline("summarization", model="t5-small")
    
    # Optional: trim to avoid token limit
    if len(text.split()) > 512:
        text = " ".join(text.split()[:512])

    result = summarizer(text, max_length=100, min_length=25, do_sample=False)[0]["summary_text"]

    with open(output_file, "w") as f:
        f.write("Natural Language Summary of Terminal Session:\n")
        f.write(result)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python3 summarize.py <log_file> <output_file>")
        sys.exit(1)

    log_file = sys.argv[1]
    output_file = sys.argv[2]

    combined_text = extract_command_blocks(log_file)
    summarize_session(combined_text, output_file)

