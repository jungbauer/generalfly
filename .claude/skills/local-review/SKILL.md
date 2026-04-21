---
name: local-review
description: Reviews a diff containing changes and produces a comments file.
disable-model-invocation: true
allowed-tools: Bash
---

1. **Setup Phase**
    - Run script `.claude/skills/local-review/scripts/review_setup.sh` the output is the directory name. An example directory name would be `local-review/LR_20260421_1248`. The formatting is `LR_YYYYMMDD_hhmm`

2. **Analysis Phase**
   - Read the generated diff file `changes.diff` inside the new directory
   - Analyzes all code changes thoroughly

3. **Review Criteria**
   - **Bugs**: Logic errors, off-by-one errors, null pointer risks
   - **Security**: Injection vulnerabilities, unsafe deserialization, hardcoded secrets
   - **Performance**: Unnecessary allocations, inefficient algorithms, N+1 queries
   - **Style**: Consistency with project conventions, naming, formatting
   - **Error Handling**: Missing try-catch, swallowed exceptions, incomplete error messages
   - **Dead Code**: Unused imports, variables, methods
   - **Tests**: Missing test coverage, brittle tests, test smells
   - **Documentation**: Missing JavaDoc, unclear comments, outdated docs

4. **Output Format**
   -  Begin with a summary of detected functionality in the diff
   -  Group findings by Severity
   -  Each finding includes:
      - **File**: Path to the file
      - **Lines**: Line number(s) affected
      - **Severity**: Critical / High / Medium / Low
      - **Category**: Bug / Security / Performance / Style / etc.
      - **Issue**: Clear description of the problem
      - **Suggestion**: Specific recommendation for improvement
   -  List positive findings and feedback
   -  Include a summary count showing the number of findings in each severity class

5. **Output Phase**
   - Display formatted review in terminal
   - Write identical content to a file `comments.md` in the same directory as `changes.diff`