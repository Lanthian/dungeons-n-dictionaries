-- Sample data taken from: https://5thsrd.org/character/languages/
INSERT INTO language (name, description, script, exotic) VALUES
    ('Non-common', 'All inhabitants of D&D are assumed to speak Common unless specified otherwise', NULL, true),
    -- Standard languages
    ('Dwarvish', 'Typically spoken by Dwarves', 'Dwarvish', false),
    ('Elvish', 'Typically spoken by Elves', 'Elvish', false),
    ('Giant', 'Typically spoken by Ogres and Giants', 'Dwarvish', false),
    ('Gnomish', 'Typically spoken by Gnomes', 'Dwarvish', false),
    ('Goblin', 'Typically spoken by Goblinoids', 'Dwarvish', false),
    ('Halfling', 'Typically spoken by Halflings', 'Common', false),
    ('Orc', 'Typically spoken by Orcs', 'Dwarvish', false),
    -- Exotic languages
    ('Abyssal', 'Typically spoken by Demons', 'Infernal', true),
    ('Celestial', 'Typically spoken by Celestials', 'Celestial', true),
    ('Draconic', 'Typically spoken by Dragons and Dragonborn', 'Draconic', true),
    ('Deep Speech', 'Typically spoken by Aboleths and Cloakers', NULL, true),
    ('Infernal', 'Typically spoken by Devils', 'Infernal', true),
    ('Primordial', 'Typically spoken by Elementals', 'Dwarvish', true),
    ('Sylvan', 'Typically spoken by Fey creatures', 'Elvish', true),
    ('Undercommon', 'Typically spoken by Underworld traders', 'Elvish', true);
