# peresed

Based on https://github.com/avast/pe_tools/blob/master/pe_tools/peresed.py but some imports are modified to allow packaging into an executable file.

## Building
    # For Linux/macOS
    pip install pyinstaller
    pip install pe_tools
    pyinstaller --onefile peresed.py

    # For Windows (Because Windows Defender believes that the file created with PyInstaller/py2exe contains a virus)
    pip install -U nuitka
    nuitka --standalone --msvc=MSVC peresed.py

