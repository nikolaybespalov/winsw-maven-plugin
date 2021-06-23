# peresed

Based on https://github.com/avast/pe_tools/blob/master/pe_tools/peresed.py but some imports are modified to allow packaging into an executable file.

## Building
    # For Linux/macOS
    pip install pyinstaller
    pip install pe_tools
    pyinstaller --onefile peresed.py

    # For Windows (Because Windows Defender believes that the file created with PyInstaller/py2exe contains a virus)
    "c:\Program Files (x86)\Python\Python38\python.exe" -m pip install -U nuitka
    "c:\Program Files (x86)\Python\Python38\python.exe" -m nuitka ^
        --onefile ^
        --standalone ^
        --msvc=14.2 ^
        --follow-imports ^
        --windows-uac-admin ^
        --windows-company-name="Nikolay Bespalov" ^
        --windows-product-name=winsw-maven-plugin ^
        --windows-product-version=0.0.1.0 ^
        --windows-file-version=0.0.1.0 ^
        --windows-file-description="An peresed.py (from pe_tools) wrapper" ^
        peresed.py


