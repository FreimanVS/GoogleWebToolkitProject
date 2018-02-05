<?xml version="1.0" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/ValCurs">
        <ValCurs>
            <xsl:for-each select="Valute/CharCode[text()='CNY' or text()='USD' or text()='EUR']">
                <Valute>
                    <CharCode>
                        <xsl:value-of select="../CharCode"/>
                    </CharCode>
                    <Value>
                        <xsl:value-of select="../Value"/>
                    </Value>
                </Valute>
            </xsl:for-each>
        </ValCurs>
    </xsl:template>
</xsl:stylesheet>
