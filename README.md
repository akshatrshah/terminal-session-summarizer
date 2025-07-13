# Terminal Session Summarizer

A Java-based custom terminal interface that logs shell commands and uses a Python ML model to generate a natural language summary of the session.

---

## Features

- Run shell commands via a Java terminal interface
- Logs each command and its output to a file
- On exit, automatically generates a concise summary of the session using a transformer-based summarization model (`t5-small`) from Hugging Face

---

## Technologies Used

- **Java** — for the terminal interface and command execution
- **Python** — for natural language summarization using Hugging Face Transformers
- **Transformers Library** — `t5-small` summarization model
- **PyTorch** — backend for the ML model

---

## Setup & Usage

### Prerequisites

- Java JDK 11 or higher
- Python 3.7+
- Python packages: `transformers`, `torch`

### Installation

1. Clone the repo:
   ```bash
   git clone https://github.com/akshatrshah/terminal-session-summarizer.git
   cd terminal-session-summarizer/TerminalLogger
