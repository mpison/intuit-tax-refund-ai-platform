# Contributing

1. Create a branch.
2. Make one focused change.
3. Build affected Maven projects.
4. Build affected Docker images.
5. Load images into Kind.
6. Deploy and smoke test.
7. Update docs.
8. Commit with a clear message.

Do not commit:
```text
target/
node_modules/
dist/
*.zip
*.log
.idea/
.vscode/
```
