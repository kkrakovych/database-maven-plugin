# Development

## File Encoding

All files in repository should be in UTF-8 encoding with Unix end of lines (`LF`).

#### Unix End of Lines for Files on Windows

The proper way to get `LF` endings in Windows is:
1. Set `core.autocrlf` to `false`:
```
git config --global core.autocrlf false
```
2. Set `core.eol` to `lf`:
```
git config --global core.eol lf
```
3. Clone repository from git to local folder.
