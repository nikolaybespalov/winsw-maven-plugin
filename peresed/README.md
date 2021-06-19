# peresed

Based on https://github.com/avast/pe_tools/blob/master/pe_tools/peresed.py but some imports are modified to allow packaging into an executable file.

## Building
    pip install pyinstaller
    pip install pe_tools
    pyinstaller --onefile peresed.py