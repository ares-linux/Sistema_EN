import json
import re

form_content = []
java_content = []

with open('/home/ares/.gemini/antigravity/brain/90cd91ed-5c1b-4b60-acc2-99e2d5ed42e0/.system_generated/logs/transcript_full.jsonl', 'r') as f:
    for line in f:
        data = json.loads(line)
        if data.get('type') == 'VIEW_FILE':
            content = data.get('content', '')
            if 'File Path: `file:///home/ares/NetBeansProjects/Sistema_EN/Sistema_en/src/sistema_en/Ventanas/V_R_Herramienta.form`' in content and 'Total Lines: 393' in content and 'Showing lines 1 to 393' in content:
                if not form_content:  # only grab the first one
                    lines = content.split('\n')
                    for l in lines:
                        if re.match(r'^\d+: ', l):
                            form_content.append(re.sub(r'^\d+: ', '', l))
            if 'File Path: `file:///home/ares/NetBeansProjects/Sistema_EN/Sistema_en/src/sistema_en/Ventanas/V_R_Herramienta.java`' in content and 'Total Lines: 604' in content and 'Showing lines 1 to 604' in content:
                if not java_content:
                    lines = content.split('\n')
                    for l in lines:
                        if re.match(r'^\d+: ', l):
                            java_content.append(re.sub(r'^\d+: ', '', l))

if form_content:
    with open('/home/ares/NetBeansProjects/Sistema_EN/Sistema_en/src/sistema_en/Ventanas/V_R_Herramienta.form', 'w') as f:
        f.write('\n'.join(form_content) + '\n')

if java_content:
    with open('/home/ares/NetBeansProjects/Sistema_EN/Sistema_en/src/sistema_en/Ventanas/V_R_Herramienta.java', 'w') as f:
        f.write('\n'.join(java_content) + '\n')

print(f"Form lines restored: {len(form_content)}, Java lines restored: {len(java_content)}")
