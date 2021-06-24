# peresed

Based on https://github.com/avast/pe_tools/blob/master/pe_tools/peresed.py but some imports are modified to allow packaging into an executable file.

## Building
    # For Linux/macOS
    pip install pyinstaller
    pip install pe_tools
    pyinstaller --onefile peresed.py

    # For Windows (Because Windows Defender believes that the file created with PyInstaller/py2exe contains a virus)
    # Use WinPython for Win7 is WinPython_3.6.3.0 (https://github.com/Nuitka/Nuitka/issues/904)
    # Otherwise it doesn't work on Windows 7
    pip install -U nuitka
    pip install -U pe_tools
    nuitka ^
        --onefile ^
        --standalone ^
        --msvc=14.2 ^
        --windows-company-name="Nikolay Bespalov" ^
        --windows-product-name=winsw-maven-plugin ^
        --windows-product-version=0.0.1.0 ^
        --windows-file-version=0.0.1.0 ^
        --windows-file-description="An peresed.py (from pe_tools) wrapper" ^
        peresed.py


